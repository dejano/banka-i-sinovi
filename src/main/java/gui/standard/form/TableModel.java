package gui.standard.form;

import database.DBConnection;
import gui.standard.Column;
import gui.standard.SortUtils;
import gui.standard.form.misc.*;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.*;

import static gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum.*;
import static gui.standard.form.misc.TableMetaData.ColumnGroupsEnum.*;

public class TableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    private TableMetaData tableMetaData;
    private Map<String, String> nextColumnCodeValues;

    private TableQueries tableQueries;

    private TableHelper tableHelper;

    public TableModel(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
        this.tableQueries = new TableQueries(tableMetaData);
        this.tableHelper = new TableHelper(tableQueries, tableMetaData);

        initColumnNames();
    }

    public TableModel(TableMetaData tableMetaData, Map<String, String> nextColumnCodeValues) {
        this(tableMetaData);

        if (!nextColumnCodeValues.isEmpty()) {
            this.nextColumnCodeValues = nextColumnCodeValues;
        }

        this.tableQueries = new TableQueries(tableMetaData, this.nextColumnCodeValues);
    }

    private void initColumnNames() {
        List<String> columnNames = tableMetaData.getColumnNames();
        @SuppressWarnings("rawtypes")
        Vector vector = new Vector(columnNames.size());
        vector.setSize(columnNames.size());
        setDataVector(vector, new Vector(columnNames));
    }

    public String getValue(int rowIndex, String columnCode) {
        return (String) getValueAt(rowIndex, tableMetaData.getColumnIndex(columnCode));
    }

    public void open() throws SQLException {
        if (nextColumnCodeValues == null) {
            fillData(tableQueries.getBasicQuery()
//                    + tableQueries.getOrderByQuery()
            );
        } else {
            fillData(tableQueries.getBasicQuery()
                            + tableQueries.getNextWhereQuery()
//                    + tableQueries.getOrderByQuery()
            );
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
                CREATE_PROCEDURE_CALL), tableHelper.getColumnList(tableMetaData.getColumns().keySet(), values));

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

        i = 0;
        for (String columnCode : tableMetaData.getColumns().keySet()) {
            columnValues.add(new Column(columnCode, values[i++]));
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

//        StatementExecutor executor = new StatementExecutor(tableMetaData.getColumnCodeTypes())
        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
                tableQueries.getBasicQuery() + tableQueries.getWhereQuery()
                        + tableQueries.getNextWhereQuery() + tableQueries.getOrderByQuery());

//        if (nextColumnCodeValues == null)
//            setParametersLike(stmt, values);
//        else
//            setParametersLike(stmt, ArrayUtils.addAll(values,
//                    nextColumnCodeValues.values().toArray(new String[nextColumnCodeValues.size()])));

        ResultSet resultSet = stmt.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            // TODO throw exception and remove else
        } else {
            this.setRowCount(0);

            List<String> rowValues = new ArrayList<>();
            while (resultSet.next()) {
                rowValues.clear();

                for (String columnName : tableMetaData.getColumns().keySet()) {
                    resultSet.getString(columnName);
                }

                addRow(rowValues.toArray());
            }
        }

        resultSet.close();
        stmt.close();

        DBConnection.getConnection().commit();

        // if (rowsAffected > 0) {
        // retVal = sortedInsert(values);
        // fireTableDataChanged();
        // }

        return retVal;
    }

    public void checkRowInsert(String[] values) throws SQLException {
        DBConnection.getConnection()
                .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        String[] result = tableHelper.getDbRowByPks(tableHelper.getPkValues(values));

        DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        if (result != null) {
            DBConnection.getConnection().commit();
            throw new SQLException(ErrorMessages.ERROR_RECORD_ALREADY_EXISTS, "", ErrorMessages.CUSTOM_ERROR_CODE);
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
            JOptionPane errorMessageDialog = new JOptionPane(errorMessage,
                    JOptionPane.ERROR_MESSAGE);
            errorMessageDialog.setVisible(true);

            DBConnection.getConnection().commit();
            throw new SQLException(errorMessage, "", ErrorMessages.CUSTOM_ERROR_CODE);
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
            throw new SQLException(errorMessage, "", ErrorMessages.CUSTOM_ERROR_CODE);
        }
    }

    private String checkUpdatedDeleted(int index, String[] result, String[] values) {
        String errorMessage = null;

        if (result != null) { // if has results
            boolean changed = false;

            // check if values changed
            for (int i = 0; i < values.length; i++) {
                if (!result[i].equals(values[i])) {
                    changed = true;
                    break;
                }
            }

            if (changed) {
                for (int i = 0; i < values.length; i++) {
                    setValueAt(result[i], index, i);
                }

                errorMessage = ErrorMessages.ERROR_RECORD_WAS_CHANGED;
            }
        } else { // already deleted
            removeRow(index);
            fireTableDataChanged();
            errorMessage = ErrorMessages.ERROR_RECORD_WAS_DELETED;
        }

        return errorMessage;
    }

    private String[] getRowValues(int index) {
        Vector rowValues = (Vector) dataVector.get(index);
        return (String[]) rowValues.toArray(new String[rowValues.size()]);
    }

    public Map<String, String> getNextColumnCodeValues() {
        return nextColumnCodeValues;
    }

}
