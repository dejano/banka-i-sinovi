package gui.standard.form;

import actions.standard.*;
import gui.standard.form.StatusBar.FormModeEnum;
import gui.standard.form.misc.ColumnMetaData;
import gui.standard.form.misc.TableMetaData;
import meta.FormMetaData;
import meta.MosquitoSingletone;
import meta.NextMetaData;
import net.miginfocom.swing.MigLayout;
import rs.mgifos.mosquito.model.MetaTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Form extends JDialog {

    private static final long serialVersionUID = 1L;

    private JButton btnDelete;
    private JButton btnNextForm;

    private JButton btnPickup;

    private JTable dataTable = new JTable();

    private TableModel tableModel;
    private DataPanel dataPanel;

    private StatusBar statusBar;
    private List<NextMetaData> nextData = new ArrayList<>();

    private Form parentForm;

    private Map<String, String> callbackZoomData;
    public Form(FormMetaData fmd) throws SQLException {
        setLayout(new MigLayout("fill"));
        setSize(new Dimension(800, 600));
        setModal(true);
        setTitle(fmd.getTitle());

        this.nextData = fmd.getNextData();

        initCenterOnScreen();
        initTable(fmd, null);
        initGui();
        initToolbar();
        initStatusBar();
    }
    public Form(FormMetaData fmd, Map<String, String> nextColumnCodeValues) throws SQLException {
        setLayout(new MigLayout("fill"));
        setSize(new Dimension(800, 600));
        setModal(true);
        setTitle(fmd.getTitle());

        this.nextData = fmd.getNextData();

        initCenterOnScreen();
        initTable(fmd, nextColumnCodeValues);
        initGui();
        initToolbar();
        initStatusBar();
    }

    private void initCenterOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
        Point newLocation = new Point(middle.x - (getWidth() / 2),
                middle.y - (getHeight() / 2));
        setLocation(newLocation);

//        setLocationByPlatform(true);

//        setLocationRelativeTo(null);
    }

    private void initToolbar() {
        JToolBar toolBar = new JToolBar();
        JButton btnSearch = new JButton(new SearchAction(this));
        toolBar.add(btnSearch);

        JButton btnRefresh = new JButton(new RefreshAction(this));
        toolBar.add(btnRefresh);

        btnPickup = new JButton(new PickupAction(this));
        toolBar.add(btnPickup);
        btnPickup.setEnabled(false);

        JButton btnHelp = new JButton(new HelpAction());
        toolBar.add(btnHelp);

        toolBar.addSeparator();

        JButton btnFirst = new JButton(new FirstAction(this));
        toolBar.add(btnFirst);

        JButton btnPrevious = new JButton(new PreviousAction(this));
        toolBar.add(btnPrevious);

        JButton btnNext = new JButton(new NextAction(this));
        toolBar.add(btnNext);

        JButton btnLast = new JButton(new LastAction(this));
        toolBar.add(btnLast);

        toolBar.addSeparator();

        JButton btnAdd = new JButton(new AddAction(this));
        toolBar.add(btnAdd);

        btnDelete = new JButton(new DeleteAction(this));
        toolBar.add(btnDelete);
        btnDelete.setEnabled(false);

        toolBar.addSeparator();

        btnNextForm = new JButton(new NextFormAction(this));
        toolBar.add(btnNextForm);
        btnNextForm.setEnabled(false);

        add(toolBar, "dock north");
    }

    private void initStatusBar() {
        statusBar = new StatusBar();

        add(statusBar, "dock south");
    }

    private void initGui() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new MigLayout("fillx"));

        dataPanel = new DataPanel();
        dataPanel.setLayout(new MigLayout("gapx 15px"));

        JPanel buttonsPanel = new JPanel();
        JButton btnCommit = new JButton(new CommitAction(this));
        JButton btnRollback = new JButton(new RollbackAction(this));

        Map<String, ColumnMetaData> columns = tableModel.getTableMetaData().getColumns();
        boolean span = false;
        for (ColumnMetaData metaColumn : columns.values()) {

            JLabel label = new JLabel(metaColumn.getName());
            String type = tableModel.getTableMetaData().getColumnCodeTypes(TableMetaData.ColumnGroupsEnum.ALL).get(metaColumn.getCode());
            System.out.println(type);
            JTextField textField = new JTextField(40);
            textField.setName(metaColumn.getCode());
            if (tableModel.getTableMetaData().getPrimaryKeyColumns().contains(metaColumn.getCode())
                    || tableModel.getTableMetaData().getZoomBaseColumns().contains(metaColumn.getCode())
                    || tableModel.getTableMetaData().getLookupColumns().containsKey(metaColumn.getCode())) {
                textField.setEditable(false);
            }


            if (tableModel.getTableMetaData().getZoomBaseColumns().contains(metaColumn.getCode())) {
                dataPanel.add(label);
                dataPanel.add(textField);
                System.out.println("TABLE:" + tableModel.getTableMetaData().getZoomTableCode(metaColumn.getCode()));
                JButton zoomBtn = new JButton(new ZoomFormAction(this, tableModel.getTableMetaData().getZoomTableCode(metaColumn.getCode())));
                dataPanel.add(zoomBtn);
                span = true;
            } else if (tableModel.getTableMetaData().getLookupColumns().containsKey(metaColumn.getCode())) {
                dataPanel.add(label);
                dataPanel.add(textField);
                span = true;
            } else {
                if (span) {
                    dataPanel.add(new JLabel(" "),"wrap");
                }
                dataPanel.add(label);
                dataPanel.add(textField, "wrap");
                span = false;
            }

        }

        bottomPanel.add(dataPanel);

        buttonsPanel.setLayout(new MigLayout("wrap"));
        buttonsPanel.add(btnCommit);
        buttonsPanel.add(btnRollback);
        bottomPanel.add(buttonsPanel, "dock east");

        add(bottomPanel, "grow, wrap");
    }

    // TODO create separate table class
    private void initTable(FormMetaData fmd, Map<String, String> nextColumnCodeValues)
            throws SQLException {
        JScrollPane scrollPane = new JScrollPane(dataTable);
        add(scrollPane, "grow, wrap");

        MetaTable metaTable = MosquitoSingletone.getInstance()
                .getMetaTable(fmd.getTableName());

        if (nextColumnCodeValues == null) {
            tableModel = new TableModel(new TableMetaData(metaTable, fmd.getCondition(),
                    fmd.getLookupMap(), fmd.getZoomData()));
        } else {
            List<String> removeColumnCodes = new ArrayList<>();
            for (String columnCode : nextColumnCodeValues.keySet()) {
                if (metaTable.getColByTableDotColumnCode(metaTable.getCode() + "." + columnCode) == null)
                    removeColumnCodes.add(columnCode);
            }
            nextColumnCodeValues.keySet().removeAll(removeColumnCodes);

            tableModel = new TableModel(new TableMetaData(metaTable, fmd.getCondition(),
                    fmd.getLookupMap(), fmd.getZoomData()), nextColumnCodeValues);
        }

        dataTable.setModel(tableModel);
        tableModel.open();

        dataTable.setRowSelectionAllowed(true);
        dataTable.setColumnSelectionAllowed(false);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting())
                            return;

                        if (Form.this.getDataTable().getSelectedRow() == -1) {
                            btnDelete.setEnabled(false);
                            btnNextForm.setEnabled(false);
                            btnPickup.setEnabled(false);
                        } else {
                            if (parentForm != null) {
                                btnPickup.setEnabled(true);
                            }
                            btnDelete.setEnabled(true);
                            btnNextForm.setEnabled(!nextData.isEmpty());
                        }
                        sync();
                    }
                });

        for (String columnCode : fmd.getHideColumns()) {
            TableColumn tableColumn = dataTable.getColumn(columnCode);
            dataTable.removeColumn(tableColumn);
        }
    }

    private void sync() {
        int index = dataTable.getSelectedRow();
        if (index < 0) {
            return;
        }
        Form.this.setMode(FormModeEnum.EDIT);
        for (Component component : dataPanel.getComponents()) {
            if (component instanceof JTextComponent) {
                JTextComponent textComponent = (JTextComponent) component;
                String value = tableModel.getValue(index, textComponent.getName());
                textComponent.setText(value);
                if (tableModel.getTableMetaData().getPrimaryKeyColumns().contains(textComponent.getName())
                        || tableModel.getTableMetaData().getLookupJoins().containsKey(textComponent.getName())) {
                    textComponent.setEditable(false);
                }
            }
        }
    }

    public String getSelectedRowValue(String columnCode) {
        int selectedRowIndex = this.dataTable.getSelectedRow();
        return this.getTableModel().getValue(selectedRowIndex, columnCode);
    }

    public void removeRow() {
        int index = dataTable.getSelectedRow();
        if (index == -1)
            return;

        int newIndex = index;

        if (index == tableModel.getRowCount() - 1)
            newIndex--;

        try {
            tableModel.deleteRow(index);

            if (tableModel.getRowCount() > 0)
                dataTable.setRowSelectionInterval(newIndex, newIndex);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Greska",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTable getDataTable() {
        return dataTable;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public FormModeEnum getMode() {
        return this.statusBar.getMode();
    }

    public void setMode(FormModeEnum mode) {
        this.statusBar.setMode(mode);
    }

    public void enablePickup() {
        btnPickup.setEnabled(true);
    }

    public void disablePickup() {
        btnPickup.setEnabled(false);
    }

    public DataPanel getDataPanel() {
        return dataPanel;
    }

    public void setDataPanel(DataPanel dataPanel) {
        this.dataPanel = dataPanel;
    }

    public List<NextMetaData> getNextData() {
        return nextData;
    }

    public void setNextData(List<NextMetaData> nextData) {
        this.nextData = nextData;
    }

    public void setParentForm(Form parentForm) {
        this.parentForm = parentForm;
    }

    public Map<String, String> getCallbackZoomData() {
        return callbackZoomData;
    }

    public void setCallbackZoomData(Map<String, String> callbackZoomData) {
        this.callbackZoomData = callbackZoomData;
    }

    public JButton getBtnPickup() {
        return btnPickup;
    }

    public Form getParentForm() {
        return parentForm;
    }

    public void onZoomDialogClosed(Map<String, String> results) {
        for (Map.Entry<String, String> entry : results.entrySet()) {
            for (Component component : getDataPanel().getComponents()) {
                if (component instanceof JTextComponent && component.getName().equals(entry.getKey())) {
                    JTextComponent textComponent = (JTextComponent) component;
                    textComponent.setText(entry.getValue());
                }
            }
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
