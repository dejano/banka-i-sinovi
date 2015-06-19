package gui.standard.form;

import actions.standard.*;
import app.AppData;
import gui.standard.form.StatusBar.FormModeEnum;
import gui.standard.form.components.ComponentCreator;
import gui.standard.form.misc.ColumnData;
import gui.standard.form.misc.FormData;
import meta.FormMetaData;
import meta.MosquitoSingletone;
import net.miginfocom.swing.MigLayout;
import rs.mgifos.mosquito.model.MetaTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gui.standard.form.StatusBar.FormModeEnum.DEFAULT;
import static gui.standard.form.StatusBar.FormModeEnum.EDIT;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.*;

public class Form extends JDialog {

    private static final long serialVersionUID = 1L;

    private JButton btnCommit;
    private JButton btnRollback;
    private JButton btnApplySearch;
    private JButton btnDelete;
    private JButton btnNextForm;
    private JButton btnPickup;

    private Form parentForm;
    private JTable dataTable = new JTable();
    private TableModel tableModel;
    private StatusBar statusBar;
    private DataPanel dataPanel;

    private FormData formData;

    private Map<String, String> callbackZoomData;

    public Form(FormMetaData fmd) throws SQLException {
        this(fmd, null);
    }

    public Form(FormMetaData fmd, Map<String, String> nextColumnCodeValues) throws SQLException {
        setLayout(new MigLayout("fill"));
        setModal(true);
        setTitle(fmd.getTitle());
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        this.setBounds(bounds);
        setResizable(false);

        MetaTable metaTable = MosquitoSingletone.getInstance()
                .getMetaTable(fmd.getTableName());

        if (nextColumnCodeValues == null)
            nextColumnCodeValues = new HashMap<>();

        nextColumnCodeValues.putAll(AppData.getInstance().getValues(fmd.getMapToAppData()));
        this.formData = new FormData(metaTable, fmd, nextColumnCodeValues);

        initCenterOnScreen();
        initTable(fmd, metaTable, nextColumnCodeValues);
        initGui(fmd);
        initToolbar(fmd);
        initStatusBar();
    }

    private void initCenterOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point newLocation = new Point(screenSize.width / 2 - (getWidth() / 2), 0);
        setLocation(newLocation);
    }

    private void initToolbar(FormMetaData fmd) {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
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
        btnAdd.setEnabled(!fmd.isReadOnly());

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
        setMode(DEFAULT);

        add(statusBar, "dock south");
    }

    private void initGui(FormMetaData fmd) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new MigLayout("fillx"));

        dataPanel = new DataPanel(this, formData, fmd.isReadOnly());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new MigLayout("wrap"));
        btnCommit = new JButton(new CommitAction(this));
        buttonsPanel.add(btnCommit);
        btnRollback = new JButton(new RollbackAction(this));
        buttonsPanel.add(btnRollback);
        btnApplySearch = new JButton(new ApplySearchAction(this));
        btnApplySearch.setVisible(false);
        buttonsPanel.add(btnApplySearch);

        if (fmd.isReadOnly()) {
            btnCommit.setVisible(false);
            btnRollback.setVisible(false);
        }

        bottomPanel.add(dataPanel);

        bottomPanel.add(buttonsPanel, "dock east");

        add(bottomPanel, "grow, wrap");
    }

    // TODO create separate table class
    private void initTable(FormMetaData fmd, MetaTable metaTable, Map<String, String> nextValues)
            throws SQLException {
        JScrollPane scrollPane = new JScrollPane(dataTable);
        add(scrollPane, "grow, wrap");

        if (nextValues == null) {
            tableModel = new TableModel(formData);
        } else {
            List<String> removeColumnCodes = new ArrayList<>();
            for (String columnCode : nextValues.keySet()) {
                Object metaColumn = metaTable.getColByTableDotColumnCode(metaTable.getCode()
                        + "." + columnCode);
                if (metaColumn == null)
                    removeColumnCodes.add(nextValues.get(columnCode));
            }
            nextValues.keySet().removeAll(removeColumnCodes);

            tableModel = new TableModel(formData);
        }

        dataTable.setModel(tableModel);
        tableModel.open();
        //adjustColumns();

        dataTable.setRowSelectionAllowed(true);
        dataTable.setColumnSelectionAllowed(false);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting())
                            return;

                        if (Form.this.getDataTable().getSelectedRow() != -1) {
                            if (parentForm != null) {
                                btnPickup.setEnabled(true);
                            }

                            btnDelete.setEnabled(!formData.isReadOnly());
                            btnNextForm.setEnabled(!formData.getNextForms().isEmpty());

                            if (!formData.isReadOnly())
                                setMode(EDIT);
                            else {
                                setMode(DEFAULT);
                            }

                            sync();
                        } else if (Form.this.getDataTable().getSelectedRow() < 0) {
                            if (getMode() == EDIT)
                                setMode(DEFAULT);
                        }
                    }
                });

        int i = 0;
        for (String columnCode : fmd.getHideColumns()) {
            int columnIndex = formData.getColumnIndex(columnCode, false);
            TableColumn tableColumn = dataTable.getColumnModel().getColumn(columnIndex - i++);
            dataTable.removeColumn(tableColumn);
        }
    }

    private void adjustColumns() {
        for (int column = 0; column < dataTable.getColumnCount(); column++) {
            TableColumn tableColumn = dataTable.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < dataTable.getRowCount(); row++) {
                TableCellRenderer cellRenderer = dataTable.getCellRenderer(row, column);
                Component c = dataTable.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + dataTable.getIntercellSpacing().width + 15;
                preferredWidth = Math.max(preferredWidth, width);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    private void sync() {
        for (Component component : dataPanel.getComponents()) {
            if (component instanceof JTextComponent) {
                ((JTextComponent) component).setText("");
                JTextComponent textComponent = (JTextComponent) component;
                String value = tableModel.getValue(dataTable.getSelectedRow(), textComponent.getName());
                textComponent.setText(value);
                ((JTextComponent) component).setEditable(formData.isEditable(component.getName()));
            }
        }
    }

    public String getSelectedRowValue(String columnCode) {
        int selectedRowIndex = this.dataTable.getSelectedRow();
        return this.getTableModel().getValue(selectedRowIndex, columnCode);
    }

    public void removeRow() throws SQLException {
        int index = dataTable.getSelectedRow();
        if (index == -1)
            return;

        int newIndex = index;

        if (index == tableModel.getRowCount() - 1)
            newIndex--;

        tableModel.deleteRow(index);

        if (tableModel.getRowCount() > 0)
            dataTable.setRowSelectionInterval(newIndex, newIndex);
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
        switch (mode) {
            case DEFAULT:
                btnApplySearch.setVisible(false);
                btnCommit.setVisible(false);
                btnRollback.setVisible(false);

                // TODO move to DataPanel
                for (Component component : dataPanel.getComponents()) {
                    if (component instanceof JTextComponent) {
                        ((JTextComponent) component).setText("");
                        ((JTextComponent) component).setEditable(false);
                    }
                }

                break;
            case ADD:
                btnApplySearch.setVisible(false);
                btnCommit.setVisible(true);
                btnRollback.setVisible(true);

                // TODO move to DataPanel?
                for (Component component : dataPanel.getComponents()) {
                    if (component instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) component;
                        String defaultValue = formData.getDefaultValue(component.getName());
                        String nextValue = formData.getNextValue(component.getName());

                        if (defaultValue != null) {
                            textComponent.setText(defaultValue);
                            textComponent.setEditable(false);
                        } else if (nextValue != null) {
                            textComponent.setText(nextValue);
                            textComponent.setEditable(false);
                        } else if (formData.isInGroup(textComponent.getName(), BASE)) {
                            textComponent.setText("");
                            textComponent.setEditable(true);
                        }
                    }
                }

                break;
            case EDIT:
                btnApplySearch.setVisible(false);
                btnCommit.setVisible(true);
                btnRollback.setVisible(true);

                break;
            case SEARCH:
                btnApplySearch.setVisible(true);
                btnCommit.setVisible(false);
                btnRollback.setVisible(false);

                // TODO move to DataPanel
                if (formData.isReadOnly()) {
                    for (Component component : dataPanel.getComponents()) {
                        if (component instanceof JTextComponent
                                && !((JTextComponent) component).isEditable()
                                && formData.isInGroup(component.getName(), BASE)) {
                            ((JTextComponent) component).setEditable(true);
                        }
                    }
                }

                break;
        }

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

    public FormData getFormData() {
        return formData;
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
