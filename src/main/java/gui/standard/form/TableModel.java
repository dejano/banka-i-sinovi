package gui.standard.form;

import database.DBConnection;
import gui.standard.Column;
import gui.standard.SortUtils;
import gui.standard.form.misc.*;
import messages.ErrorMessages;
import messages.WarningMessages;


import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum.*;
import static gui.standard.form.misc.TableMetaData.ColumnGroupsEnum.ALL_WITHOUT_LOOKUP;
import static gui.standard.form.misc.TableMetaData.ColumnGroupsEnum.PRIMARY_KEYS;

public class TableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    private TableMetaData tableMetaData;
    private Map<String, String> nextColumnCodeValues;

    private TableQueriesBuilder tableQueriesBuilder;

    private TableHelper tableHelper;

    public TableModel(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
        this.tableQueriesBuilder = new TableQueriesBuilder(tableMetaData);
        this.tableHelper = new TableHelper(tableQueriesBuilder, tableMetaData);

        initColumnNames();
    }

    public TableModel(TableMetaData tableMetaData, Map<String, String> nextColumnCodeValues) {
        this(tableMetaData);

        if (!nextColumnCodeValues.isEmpty()) {
            this.nextColumnCodeValues = nextColumnCodeValues;
        }

        this.tableQueriesBuilder = new TableQueriesBuilder(tableMetaData, this.nextColumnCodeValues);
    }

    private void initColumnNames() {
        List<String> columnNames = tableMetaData.getColumnNames();
        @SuppressWarnings("rawtypes")
        Vector vector = new Vector(columnNames.size());
        vector.setSize(columnNames.size());
        setDataVector(vector, new Vector(columnNames));

        setColumnIdentifiers(tableMetaData.getColumnCodes().toArray());
    }

    public String getValue(int rowIndex, String columnCode) {
        return (String) getValueAt(rowIndex, tableMetaData.getColumnIndex(columnCode));
    }

    public void open() throws SQLException {
        if (nextColumnCodeValues == null) {
            fillData(tableQueriesBuilder.getBasicQuery().getOrderByQuery().build());
        } else {
            fillData(tableQueriesBuilder.getBasicQuery()
                    .getNextWhereQuery()
                    .getOrderByQuery()
                    .build());
        }
    }

    private void fillData(String query) throws SQLException {
        setRowCount(0);

        List<String[]> results;
        StatementExecutor executor = new StatementExecutor(tableMetaData.getColumnCodeTypes(ALL_WITHOUT_LOOKUP));
        if (nextColumnCodeValues != null) {
            results = executor.execute(query, tableMetaData.getColumns().keySet(), nextColumnCodeValues);
        } else {
            results = executor.execute(query, tableMetaData.getColumns().keySet());
        }

        for (String[] rowValues : results) {
            addRow(rowValues);
        }

        fireTableDataChanged();
    }

    public int insertRow(String[] values) throws SQLException {
        int retVal;

        checkRowInsert(values);

        StatementExecutor executor = new StatementExecutor(
                tableMetaData.getColumnCodeTypes(ALL_WITHOUT_LOOKUP));

        executor.executeProcedure(ProcedureCallFactory.getProcedureCall(tableMetaData.getTableName(),
                CREATE_PROCEDURE_CALL), tableHelper.getColumnList(tableMetaData.getBaseColumns().keySet(), values));

        retVal = sortedInsert(values);
        fireTableDataChanged();

        return retVal;
    }

    public int updateRow(int rowIndex, String[] values) throws SQLException {
        int retVal;

        checkRowUpdate(rowIndex, values);

        String[] pkValues = tableHelper.getPkValues(getRowValues(rowIndex));

        List<Column> columnValues = new ArrayList<>();
        int i = 0;
        for (String pkColumnCode : tableMetaData.getPrimaryKeyColumns()) {
            columnValues.add(new Column(pkColumnCode, pkValues[i++]));
        }

        for (String columnCode : tableMetaData.getBaseColumns().keySet()) {
            int colIndex = tableMetaData.getColumnIndex(columnCode);
            columnValues.add(new Column(columnCode, values[colIndex]));
        }

        StatementExecutor executor = new StatementExecutor(tableMetaData.getColumnCodeTypes(ALL_WITHOUT_LOOKUP));
        executor.executeProcedure(ProcedureCallFactory.getProcedureCall(tableMetaData.getTableName(),
                UPDATE_PROCEDURE_CALL), columnValues);

        removeRow(rowIndex);
        retVal = sortedInsert(values);
        fireTableDataChanged();

        return retVal;
    }

    private int sortedInsert(String[] values) {
        int left = 0;
        int right = getRowCount() - 1;
        int mid;

        while (left <= right) {
            mid = (left + right) / 2;

            int compareResult = comparePkValues(values, mid);

            if (compareResult > 0)
                left = mid + 1;
            else if (compareResult < 0)
                right = mid - 1;
            else
                break;
        }

        insertRow(left, values);

        return left;
    }

    private int comparePkValues(String[] values, int rowIndex) {
        int ret = 0;

        for (String columnCode : tableMetaData.getPrimaryKeyColumns()) {
            int columnIndex = tableMetaData.getColumnIndex(columnCode);

            String value = values[columnIndex];
            String tableValue = (String) getValueAt(rowIndex, columnIndex);

            int compareResult;
            try {
                double numValue = Double.parseDouble(value);
                double numTableValue = Double.parseDouble(tableValue);

                compareResult = Double.compare(numValue, numTableValue);
            } catch (NumberFormatException e) {
                compareResult = SortUtils.getLatCyrCollator().compare(value, tableValue);
            }

            if (compareResult != 0) {
                ret = compareResult;
                break;
            }
        }

        return ret;
    }

    public void deleteRow(int index) throws SQLException {
        checkRowDelete(index);

        String[] pkValues = tableHelper.getPkValues(getRowValues(index));
        List<Column> columnValues =
                tableHelper.getColumnList(tableMetaData.getPrimaryKeyColumns(), pkValues);

        StatementExecutor executor = new StatementExecutor(tableMetaData.getColumnCodeTypes(PRIMARY_KEYS));
        executor.executeProcedure(ProcedureCallFactory.getProcedureCall(tableMetaData.getTableName(),
                DELETE_PROCEDURE_CALL), columnValues);

        removeRow(index);
        fireTableDataChanged();
    }

    public int search(String[] values) throws SQLException {
        int retVal = 0;

        StatementExecutor executor = new StatementExecutor(tableMetaData.getColumnCodeTypes(ALL_WITHOUT_LOOKUP));
        executor.setFuzzy(true);
        List<String[]> results = executor.execute(
                tableQueriesBuilder.getBasicQuery().getWhereLikeQuery().getOrderByQuery().build(),
                tableMetaData.getColumns().keySet(),
                tableHelper.createMap(tableMetaData.getColumns().keySet(), values));

        if (results.isEmpty())
            throw new SQLException(WarningMessages.SEARCH_NO_RESULTS, "",
                    WarningMessages.CUSTOM_CODE);

        this.setRowCount(0);
        for (String[] rowValues : results) {
            addRow(rowValues);
        }

        fireTableDataChanged();

        return retVal;
    }

    public void checkRowInsert(String[] values) throws SQLException {
        DBConnection.getConnection()
                .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        String[] result = tableHelper.getDbRowByPks(tableHelper.getPkValues1(values));

        DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        if (result != null) {
            DBConnection.getConnection().commit();
            throw new SQLException(ErrorMessages.RECORD_ALREADY_EXISTS, "",
                    ErrorMessages.CUSTOM_CODE);
        }
    }

    public void checkRowUpdate(int index, String[] newValues) throws SQLException {
        DBConnection.getConnection()
                .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        String[] oldPkValues = tableHelper.getPkValues(getRowValues(index));
        String[] result = tableHelper.getDbRowByPks(oldPkValues);

        String errorMessage = checkUpdatedDeleted(index, result, getRowValues(index));

        String[] newPkValues = tableHelper.getPkValues(newValues);

        boolean newKey = false;
        int i = 0;
        for (String oldPkValue : oldPkValues) {
            if (!oldPkValue.equals(newPkValues[i++])) {
                newKey = true;
                break;
            }
        }

        if (errorMessage == null && newKey)
            checkRowInsert(newValues);

        DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        if (errorMessage != null) {
            DBConnection.getConnection().commit();
            throw new SQLException(errorMessage, "", ErrorMessages.CUSTOM_CODE);
        }
    }

    public void checkRowDelete(int index) throws SQLException {
        DBConnection.getConnection()
                .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        String[] values = getRowValues(index);
        String[] result = tableHelper.getDbRowByPks(tableHelper.getPkValues(values));

        DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        String errorMessage = null;
        if (result != null)
            errorMessage = checkUpdatedDeleted(index, result, values);

        if (errorMessage != null) {
            DBConnection.getConnection().commit();
            throw new SQLException(errorMessage, "", ErrorMessages.CUSTOM_CODE);
        }
    }

    private String checkUpdatedDeleted(int index, String[] result, String[] values) {
        String errorMessage = null;

        if (result != null) { // if has results
            boolean changed = false;

            // check if values changed
            for (int i = 0; i < values.length; i++) {
                String resultValue = result[i];
                String value = values[i];

                if ((resultValue != null && !resultValue.equals(value))
                        || (value != null && !value.equals(resultValue))) {
                    changed = true;
                    break;
                }
            }

            if (changed) {
                for (int i = 0; i < values.length; i++) {
                    setValueAt(result[i], index, i);
                }

                errorMessage = ErrorMessages.RECORD_WAS_CHANGED;
            }
        } else { // already deleted
            removeRow(index);
            fireTableDataChanged();
            errorMessage = ErrorMessages.RECORD_WAS_DELETED;
        }

        return errorMessage;
    }

    public String[] getRowValues(int index) {
        Vector rowValues = (Vector) dataVector.get(index);
        return (String[]) rowValues.toArray(new String[rowValues.size()]);
    }

    public Map<String, String> getNextColumnCodeValues() {
        return nextColumnCodeValues;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public TableHelper getTableHelper() {
        return tableHelper;
    }
}
