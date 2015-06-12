package controller;

import com.google.inject.assistedinject.Assisted;
import db.DatabaseStorage;
import model.TableModel;
import model.db.SchemaRow;
import view.Table;

import javax.inject.Inject;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Observable;

public class TableController extends Observable implements ListSelectionListener {

    private final Table view;
    private SchemaRow schemaRow;
    private DatabaseStorage storage;
    private TableModel tableModel;

    @Inject
    public TableController(@Assisted TableModel tableModel, @Assisted SchemaRow schemaRow, DatabaseStorage storage) {
        this.schemaRow = schemaRow;
        this.storage = storage;
        this.tableModel = tableModel;
        this.view = new Table(tableModel);
        this.view.getSelectionModel().addListSelectionListener(this);
        populateTable();
    }

    public void refresh() {
        System.out.println("TableController.refresh");
        tableModel.setRowCount(0);
        populateTable();
    }

    private void populateTable() {
        try {
            List<String[]> rows = storage.select(schemaRow);
            tableModel.addRows(rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSelectedRowIndex() {
        return view.getSelectedRow();
    }

    public String[] getSelectedRowData() {
        return tableModel.getRowValues(view.getSelectedRow());
    }

    public String getColumnValueByCode(int index, String code) {
        return tableModel.getColumnValueByCode(index, code);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting())
            return;
        int index = getSelectedRowIndex();
        notifyChanged(index);
    }

    public void notifyChanged(Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public Table getView() {
        return view;
    }
}
