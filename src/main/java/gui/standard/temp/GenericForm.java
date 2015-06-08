package gui.standard.temp;

import javax.swing.*;

public class GenericForm extends JDialog {

//    private static final long serialVersionUID = 1L;
//
//    private JPanel buttonsPanel;
//    private List<gui.standard.temp.model.Column> columnList;
//    private MetaTable metaTable;
//    private GenericTableModel tableModel;
//    private FormMetaData fmd;
//    private Table table;
//
//    private JToolBar toolBar;
//
//    private JButton btnAdd, btnCommit, btnDelete, btnFirst, btnLast, btnHelp,
//            btnNext, btnNextForm, btnPickup, btnRefresh, btnRollback,
//            btnSearch, btnPrevious;
//
//    private JTable dataTable = new JTable();
//
//    private List<JComponent> components;
//
//    private DataPanel dataPanel;
//    private StatusBar statusBar;
//
//    private ColumnList zoomList;
//
//    public GenericForm(FormMetaData fmd) {
//        this.fmd = fmd;
//        setLayout(new MigLayout("fill"));
//        setSize(new Dimension(800, 600));
//        setModal(true);
//        setLocationRelativeTo(null);
//        setTitle(fmd.getTitle());
//
//        initModel();
//        initTable(fmd);
//        getData();
//
//        JScrollPane scrollPane = new JScrollPane(dataTable);
//        add(scrollPane, "grow, wrap");
//
//
//        initGui();
//        initToolbar();
//        initStatusBar();
//    }
//
//    private void initToolbar() {
//        toolBar = new JToolBar();
//        btnSearch = new JButton(new SearchAction(this));
//        toolBar.add(btnSearch);
//
//        btnRefresh = new JButton(new RefreshAction(this));
//        toolBar.add(btnRefresh);
//
//        btnPickup = new JButton(new PickupAction(this));
//        toolBar.add(btnPickup);
//        btnPickup.setEnabled(false);
//
//        btnHelp = new JButton(new HelpAction());
//        toolBar.add(btnHelp);
//
//        toolBar.addSeparator();
//
//        btnFirst = new JButton(new FirstAction(this));
//        toolBar.add(btnFirst);
//
//        btnPrevious = new JButton(new PreviousAction(this));
//        toolBar.add(btnPrevious);
//
//        btnNext = new JButton(new NextAction(this));
//        toolBar.add(btnNext);
//
//        btnLast = new JButton(new LastAction(this));
//        toolBar.add(btnLast);
//
//        toolBar.addSeparator();
//
//        btnAdd = new JButton(new AddAction(this));
//        toolBar.add(btnAdd);
//
//        btnDelete = new JButton(new DeleteAction(this));
//        toolBar.add(btnDelete);
//
//        toolBar.addSeparator();
//
//        btnNextForm = new JButton(new NextFormAction(this));
//        toolBar.add(btnNextForm);
//
//        add(toolBar, "dock north");
//    }
//
//    private void initStatusBar() {
//        statusBar = new StatusBar();
//
//        add(statusBar, "dock south");
//    }
//
//    private void initGui() {
//        components = new ArrayList<>();
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setLayout(new MigLayout("fillx"));
//
//        dataPanel = new DataPanel();
//        dataPanel.setLayout(new MigLayout("gapx 15px"));
//
//        JPanel buttonsPanel = new JPanel();
//        btnCommit = new JButton();
//        btnRollback = new JButton(new RollbackAction(this));
//
//        List<gui.standard.temp.model.Column> columns = table.getColumns();
//        for (final gui.standard.temp.model.Column column : columns) {
//
//            JLabel label = new JLabel(column.getName());
//            JTextField textField = new JTextField(40);
//            textField.setName(column.getCode());
//            if (table.getPrimaryKeys().contains(column)) {
//                textField.setEditable(false);
//            }
//
//            if (table.getForeignKeys().containsKey(column)) {
//                dataPanel.add(label);
//                dataPanel.add(textField);
//                System.out.println(column);
//                JButton button = new JButton("...");
//                button.addActionListener(new AbstractAction() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        Gson gson = new Gson();
//                        FormMetaData fmd = null;
//                        BufferedReader br = null;
//                        try {
//                            br = new BufferedReader(
//                                    new FileReader("src/main/resources/" + column.getReferencedTableName() + ".json"));
//                        } catch (FileNotFoundException e1) {
//                            e1.printStackTrace();
//                        }
//                        fmd = gson.fromJson(br, FormMetaData.class);
//                        GenericForm form = new GenericForm(fmd);
//                        form.setVisible(true);
//                    }
//                });
//                dataPanel.add(button);
//            } else if (table.getForeignKeys().containsValue(column)) {
//                dataPanel.add(textField, "wrap");
//            } else {
//                dataPanel.add(label);
//                dataPanel.add(textField, "wrap");
//            }
//        }
//
//        bottomPanel.add(dataPanel);
//
//        buttonsPanel.setLayout(new MigLayout("wrap"));
//        buttonsPanel.add(btnCommit);
//        buttonsPanel.add(btnRollback);
//        bottomPanel.add(buttonsPanel, "dock east");
//
//        add(bottomPanel, "grow, wrap");
//    }
//
//    private void initTable(FormMetaData fmd) {
//        Vector<String> columnNames = new Vector<String>();
//        for (gui.standard.temp.model.Column column : columnList) {
//            columnNames.add(column.getName());
//        }
//        tableModel = new GenericTableModel(columnNames, 0, table);
//
//        dataTable = new JTable(tableModel);
//
//
//        dataTable.setRowSelectionAllowed(true);
//        dataTable.setColumnSelectionAllowed(false);
//        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        dataTable.getSelectionModel().addListSelectionListener(
//                new ListSelectionListener() {
//                    public void valueChanged(ListSelectionEvent e) {
//                        if (e.getValueIsAdjusting())
//                            return;
//
//                        GenericForm.this.setMode(StatusBar.FormModeEnum.EDIT);
//                        sync();
//                    }
//                });
//    }
//
//    private void sync() {
//        int index = dataTable.getSelectedRow();
//        if (index < 0) {
//            return;
//        }
//
//        for (Component component : dataPanel.getComponents()) {
//            if (component instanceof JTextComponent) {
//                JTextComponent textComponent = (JTextComponent) component;
//                String value = tableModel.getColumnValue(index, textComponent.getName());
//                textComponent.setText(value);
//            }
//        }
//    }
//
//    public void removeRow() {
//        int index = dataTable.getSelectedRow();
//        if (index == -1)
//            return;
//
//        int newIndex = index;
//
//        if (index == tableModel.getRowCount() - 1)
//            newIndex--;
//
////		try {
//        tableModel.deleteRow(index);
//
//        if (tableModel.getRowCount() > 0)
//            dataTable.setRowSelectionInterval(newIndex, newIndex);
////		} catch (SQLException ex) {
////			JOptionPane.showMessageDialog(this, ex.getMessage(), "Greska",
////					JOptionPane.ERROR_MESSAGE);
////		}
//    }
//
//    public JTable getDataTable() {
//        return dataTable;
//    }
//
//    public GenericTableModel getTableModel() {
//        return tableModel;
//    }
//
//    public FormModeEnum getMode() {
//        return this.statusBar.getMode();
//    }
//
//    public void setMode(FormModeEnum mode) {
//        this.statusBar.setMode(mode);
//    }
//
//    public void enablePickup() {
//        btnPickup.setEnabled(true);
//    }
//
//    public void disablePickup() {
//        btnPickup.setEnabled(false);
//    }
//
//    public ColumnList getColumnList() {
//        return zoomList;
//    }
//
//    public DataPanel getDataPanel() {
//        return dataPanel;
//    }
//
//    public void setDataPanel(DataPanel dataPanel) {
//        this.dataPanel = dataPanel;
//    }
//
//    private void initModel() {
//        metaTable = MosquitoSingletone.getInstance()
//                .getMetaTable(fmd.getTableName());
//        String tableCode = metaTable.getCode();
//
//        Vector<String> columnNames = new Vector<String>();
//        @SuppressWarnings("unchecked")
//        Vector<MetaColumn> metaColumns = (Vector<MetaColumn>) metaTable.cColumns();
//
//        columnList = new ArrayList<gui.standard.temp.model.Column>();
//        List<gui.standard.temp.model.Column> primaryKeys = new ArrayList<gui.standard.temp.model.Column>();
//        Map<gui.standard.temp.model.Column, gui.standard.temp.model.Column> foreignKeys = new HashMap<gui.standard.temp.model.Column, gui.standard.temp.model.Column>();
//        for (int i = 0; i < metaTable.getTotalColumns(); i++) {
//            gui.standard.temp.model.Column column = null;
//            MetaColumn metaColumn = metaColumns.get(i);
//            boolean fk = metaColumn.isPartOfFK();
//            boolean pk = metaColumn.isPartOfPK();
//            column = new gui.standard.temp.model.Column(metaColumn.getName(), metaColumn.getCode(), pk, fk);
//            columnList.add(column);
//            if (fk) {
//                for (Lookup lookup : fmd.getLookups()) {
//                    if (lookup.getFrom().equals(metaColumn.getCode())) {
//                        for (Lookup.LookupColumn lookupColumn : lookup.getColumns()) {
//                            gui.standard.temp.model.Column refColumn = new gui.standard.temp.model.Column(lookupColumn.getLabel(), lookupColumn.getName(), true, false);
//                            column.setReferencedTableName(lookup.getTable());
//                            columnList.add(refColumn);
//                            foreignKeys.put(column, refColumn);
//                        }
//                    }
//                }
//            }
//
//            if (pk) {
//                primaryKeys.add(column);
//            }
//        }
//
//        table = new Table(metaTable.getCode(), metaTable.getName(), columnList, primaryKeys, foreignKeys);
////        Enumeration enumeration = metaTable.eRefs();
////        while(enumeration.hasMoreElements()) {
////            MetaReference obj = (MetaReference)enumeration.nextElement();
////            System.out.println(obj.getName());
////            System.out.println(obj.getCode());
////        }
//    }
//
//    public void getData() {
//        tableModel.setRowCount(0);
//        QueryBuilder queryBuilder = new QueryBuilder();
//        try {
//            queryBuilder.select(metaTable);
//            for (Lookup lookup : fmd.getLookups()) {
//                queryBuilder.with(metaTable.getCode(), lookup.getTable(), lookup.getColumnNames(), lookup.getFrom(), lookup.getTo());
//            }
//            List<String[]> list = queryBuilder.from(metaTable.getCode())
//                    .execute();
//            for (String[] strings : list) {
//                tableModel.addRow(strings);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.out.println(queryBuilder);
//    queryBuilder}
}
