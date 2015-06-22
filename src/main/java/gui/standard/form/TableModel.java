package gui.standard.form;

import database.DBConnection;
import gui.standard.ColumnValue;
import gui.standard.form.misc.*;
import messages.ErrorMessages;
import messages.WarningMessages;
import util.ValueMapper;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.*;
import static gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum.*;
import static gui.standard.form.misc.StatementExecutor.BOOLEAN;
import static gui.standard.form.misc.StatementExecutor.DATE;

public class TableModel extends DefaultTableModel {

    private static final long serialVersionUID = 1L;

    private FormData formData;
    private TableQueriesBuilder tableQueriesBuilder;

    // TODO move
    public String[] getDbRowByPks(String[] pkValues) throws SQLException {
        String[] ret = null;

        Map<String, String> columnCodeTypes = formData.getColumnCodeTypes(PRIMARY_KEYS);
        StatementExecutor executor = new StatementExecutor(columnCodeTypes);

        List<String[]> results = executor.execute(
                tableQueriesBuilder.getBasicQuery().getWhereByPksQuery().build(),
                formData.mapValues(PRIMARY_KEYS, pkValues),
                formData.getColumnCodes(ALL));

        if (!results.isEmpty())
            ret = results.get(0);

        return ret;
    }

    public TableModel(FormData formData) {
        this.formData = formData;
        this.tableQueriesBuilder = new TableQueriesBuilder(formData);

        initColumnNames();
    }

    private void initColumnNames() {
        List<String> columnNames = formData.getColumnNames();
        @SuppressWarnings("rawtypes")
        Vector vector = new Vector(columnNames.size());
        vector.setSize(columnNames.size());
        setDataVector(vector, new Vector(columnNames));
//        setColumnIdentifiers(formData.getColumnCodes(ALL).toArray());
    }

    public String getValue(int rowIndex, String columnCode) {
        return (String) getValueAt(rowIndex, formData.getColumnIndex(columnCode, false));
    }

    public void open() throws SQLException {
        fillData(tableQueriesBuilder.getBasicQuery()
                .getNextWhereQuery()
                .getOrderByQuery()
                .build());
    }

    private void fillData(QueryBuilder.Query query) throws SQLException {
        setRowCount(0);

        List<String[]> results;
        StatementExecutor executor = new StatementExecutor(formData.getColumnCodeTypes(BASE));
        results = executor.execute(query, formData.getNextValues(), formData.getColumnCodes(ALL));

        for (String[] rowValues : results) {
            addRow(transformValues(rowValues));
        }

        fireTableDataChanged();
    }

    public int insertRow(String[] newValues) throws SQLException {
        int retVal;

        checkRowInsert(newValues);

        StatementExecutor executor = new StatementExecutor(
                formData.getColumnCodeTypes(BASE));

        executor.executeProcedure(ProcedureCallFactory.getProcedureCall(formData,
                CREATE_PROCEDURE_CALL), formData.mapValues(BASE, newValues));

        String[] valuesWithLookup = getDbRowByPks(formData.extractPkValues(newValues, true));
        retVal = sortedInsert(valuesWithLookup);

        fireTableDataChanged();

        return retVal;
    }

    public int updateRow(int rowIndex, String[] newValues) throws SQLException {
        int retVal;

        checkRowUpdate(rowIndex, newValues);

        String[] pkValues = formData.extractPkValues(getRowValues(rowIndex), true);

        List<ColumnValue> columnValueValues = new ArrayList<>();
        int i = 0;
        for (String pkColumnCode : formData.getColumnCodes(PRIMARY_KEYS)) {
            columnValueValues.add(new ColumnValue(pkColumnCode, pkValues[i++]));
        }

        for (String columnCode : formData.getColumnCodes(BASE)) {
            int colIndex = formData.getColumnIndex(columnCode, true);
            columnValueValues.add(new ColumnValue(columnCode, newValues[colIndex]));
        }

        StatementExecutor executor = new StatementExecutor(formData.getColumnCodeTypes(BASE));
        executor.executeProcedure(ProcedureCallFactory.getProcedureCall(formData,
                UPDATE_PROCEDURE_CALL), columnValueValues);

        removeRow(rowIndex);

        String[] valuesWithLookup = getDbRowByPks(formData.extractPkValues(newValues, true));
        retVal = sortedInsert(valuesWithLookup);

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

        insertRow(left, transformValues(values));

        return left;
    }

    private String[] transformValues(String[] values) {
        String[] ret = new String[values.length];

        List<ColumnData> columns = formData.getColumns(ALL);

        for (int i = 0; i < values.length; i++) {
            ColumnData columnData = columns.get(i);

            if (columnData.isType(BOOLEAN)) {
                ret[i] = ValueMapper.mapBoolean(values[i]);
            } else if (columnData.isType(DATE)) {
                ret[i] = ValueMapper.mapDate(values[i]);
            } else {
                ret[i] = values[i];
            }
        }

        return ret;
    }

    private int comparePkValues(String[] values, int rowIndex) {
        int ret = 0;

        for (String columnCode : formData.getColumnCodes(PRIMARY_KEYS)) {
            int columnIndex = formData.getColumnIndex(columnCode, false);

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

        String[] pkValues = formData.extractPkValues(getRowValues(index), false);
        List<ColumnValue> columnValueValues = formData.mapValues(PRIMARY_KEYS, pkValues);

        StatementExecutor executor = new StatementExecutor(formData.getColumnCodeTypes(PRIMARY_KEYS));
        executor.executeProcedure(ProcedureCallFactory.getProcedureCall(formData,
                DELETE_PROCEDURE_CALL), columnValueValues);

        removeRow(index);
        fireTableDataChanged();
    }

    public int search(String[] searchValues) throws SQLException {
        int retVal = 0;

        StatementExecutor executor = new StatementExecutor(formData.getColumnCodeTypes(BASE));
        List<ColumnValue> values = new ArrayList<>(formData.getNextValues());
        values.addAll(formData.mapValues(ALL, searchValues));
        List<String[]> results = executor.execute(tableQueriesBuilder.getBasicQuery().getNextWhereQuery()
                        .getWhereLikeQuery().getOrderByQuery().build(),
                values, formData.getColumnCodes(ALL));

        if (results.isEmpty())
            throw new SQLException(WarningMessages.SEARCH_NO_RESULTS, "",
                    WarningMessages.CUSTOM_CODE);

        this.setRowCount(0);
        for (String[] rowValues : results) {
            addRow(transformValues(rowValues));
        }

        fireTableDataChanged();

        return retVal;
    }

    public void checkRowInsert(String[] values) throws SQLException {
        DBConnection.getConnection()
                .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        String[] result = getDbRowByPks(formData.extractPkValues(values, true));

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

        String[] oldPkValues = formData.extractPkValues(getRowValues(index), true);
        String[] result = getDbRowByPks(oldPkValues);

        String errorMessage = checkUpdatedDeleted(index, transformValues(result), getRowValues(index));

        String[] newPkValues = formData.extractPkValues(newValues, true);

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
        String[] result = getDbRowByPks(formData.extractPkValues(values, false));

        DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        String errorMessage = null;
        if (result != null)
            errorMessage = checkUpdatedDeleted(index, transformValues(result), values);

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

    public List<ColumnValue> getNextColumnCodeValues() {
        return formData.getNextValues();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public FormData getFormData() {
        return formData;
    }
}
