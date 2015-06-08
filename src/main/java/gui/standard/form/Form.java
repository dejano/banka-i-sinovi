package gui.standard.form;

import actions.standard.*;
import gui.standard.Column;
import gui.standard.ColumnList;
import gui.standard.form.StatusBar.FormModeEnum;
import meta.FormMetaData;
import meta.MosquitoSingletone;
import meta.NextMetaData;
import net.miginfocom.swing.MigLayout;
import rs.mgifos.mosquito.model.MetaTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Form extends JDialog {

    private static final long serialVersionUID = 1L;

    private JToolBar toolBar;

    private JButton btnAdd, btnCommit, btnDelete, btnFirst, btnLast, btnHelp,
            btnNext, btnNextForm, btnPickup, btnRefresh, btnRollback,
            btnSearch, btnPrevious;
    private List<JButton> selectionSensitiveButtons = new ArrayList<>();

    private JTable dataTable = new JTable();
    private TableModel tableModel;

    private DataPanel dataPanel;
    private StatusBar statusBar;

    private ColumnList zoomList;
    private List<NextMetaData> nextData = new ArrayList<>();

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
        toolBar = new JToolBar();
        btnSearch = new JButton(new SearchAction(this));
        toolBar.add(btnSearch);

        btnRefresh = new JButton(new RefreshAction(this));
        toolBar.add(btnRefresh);

        btnPickup = new JButton(new PickupAction(this));
        toolBar.add(btnPickup);
        btnPickup.setEnabled(false);

        btnHelp = new JButton(new HelpAction());
        toolBar.add(btnHelp);

        toolBar.addSeparator();

        btnFirst = new JButton(new FirstAction(this));
        toolBar.add(btnFirst);

        btnPrevious = new JButton(new PreviousAction(this));
        toolBar.add(btnPrevious);

        btnNext = new JButton(new NextAction(this));
        toolBar.add(btnNext);

        btnLast = new JButton(new LastAction(this));
        toolBar.add(btnLast);

        toolBar.addSeparator();

        btnAdd = new JButton(new AddAction(this));
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
        btnCommit = new JButton(new CommitAction(this));
        btnRollback = new JButton(new RollbackAction(this));

        // JLabel lblSifra = new JLabel("Šifra države:");
        // JLabel lblNaziv = new JLabel("Naziv države:");
        //
        // dataPanel.add(lblSifra);
        // // dataPanel.add(tfSifra, "wrap");
        // dataPanel.add(lblNaziv);
        // // dataPanel.add(tfNaziv);
        // bottomPanel.add(dataPanel);

        buttonsPanel.setLayout(new MigLayout("wrap"));
        buttonsPanel.add(btnCommit);
        buttonsPanel.add(btnRollback);
        bottomPanel.add(buttonsPanel, "dock east");

        add(bottomPanel, "grow, wrap");
    }

    private void initTable(FormMetaData fmd, Map<String, String> nextColumnCodeValues) throws SQLException {
        JScrollPane scrollPane = new JScrollPane(dataTable);
        add(scrollPane, "grow, wrap");

        MetaTable metaTable = MosquitoSingletone.getInstance()
                .getMetaTable(fmd.getTableName());

        if (nextColumnCodeValues == null) {
            tableModel = new TableModel(metaTable);
        } else {
            List<String> removeColumnCodes = new ArrayList<>();
            for (String columnCode : nextColumnCodeValues.keySet()) {
                if (metaTable.getColByTableDotColumnCode(metaTable.getCode() + "." + columnCode) == null)
                    removeColumnCodes.add(columnCode);
            }
            nextColumnCodeValues.keySet().removeAll(removeColumnCodes);

            tableModel = new TableModel(metaTable, nextColumnCodeValues);
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
                        } else {
                            btnDelete.setEnabled(true);
                            btnNextForm.setEnabled(!nextData.isEmpty());
                        }

                        Form.this.setMode(FormModeEnum.EDIT);
                        sync();
                    }
                });
    }

    private void sync() {
        int index = dataTable.getSelectedRow();
        if (index < 0) {
            // tfSifra.setText("");
            // tfNaziv.setText("");
            return;
        }
        // String sifra = (String) tableModel.getValueAt(index, 0);
        // String naziv = (String) tableModel.getValueAt(index, 1);
        // tfSifra.setText(sifra);
        // tfNaziv.setText(naziv);
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

    public void createZoomList() {
        int selectedRowIndex = dataTable.getSelectedRow();
        int columnCount = tableModel.getColumnCount();

        zoomList = new ColumnList();

        for (int i = 0; i < columnCount; i++) {
            Column column = new Column(tableModel.getColumnName(i),
                    tableModel.getValueAt(selectedRowIndex, i));
            zoomList.add(column);
        }
    }

    public void createNextList() {
        int selectedRowIndex = dataTable.getSelectedRow();

        zoomList = new ColumnList();

        zoomList.add(new Column("drzava.dr_sifra", tableModel.getValueAt(
                selectedRowIndex, 0)));
        zoomList.add(new Column("drzava.dr_naziv", tableModel.getValueAt(
                selectedRowIndex, 1)));
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

    public ColumnList getColumnList() {
        return zoomList;
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
}
