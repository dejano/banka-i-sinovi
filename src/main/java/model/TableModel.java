package model;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Vector;

/**
 * Created by dejan on 6/9/2015.
 */
public class TableModel extends DefaultTableModel {


    private Vector columnCodes;

    public TableModel(Vector columnNames,Vector columnCodes, int rowCount) {
        super(columnNames, rowCount);
        this.columnCodes = columnCodes;
    }

    public void addRows(List<String[]> rows) {
        for (String[] row : rows) {
            addRow(row);
        }
    }

    public String getColumnValueByName(int index, String name) {
        for (int i = 0; i < columnIdentifiers.size(); i++) {
            String columnName = (String) columnIdentifiers.get(i);
            if (columnName.equals(name)) {
                return String.valueOf(getValueAt(index, i));
            }
        }

        return null;
    }

    public String getColumnValueByCode(int index, String code) {
        System.out.println();
        for (int i = 0; i < columnCodes.size(); i++) {
            String columnName = (String) columnCodes.get(i);
            System.out.println(columnName +":"+ code);
            if (columnName.equals(code)) {
                System.out.println();
                return String.valueOf(getValueAt(index, i));
            }
        }
        System.out.println();

        return null;
    }

    public String[] getRowValues(int index) {
        Vector rowValues = (Vector) dataVector.get(index);
        return (String[]) rowValues.toArray(new String[rowValues.size()]);
    }
}
