package gui.standard.form;

import database.DBConnection;
import gui.standard.SortUtils;
import org.apache.commons.lang3.ArrayUtils;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.*;

public class TableModel extends DefaultTableModel {

    public static final int CUSTOM_ERROR_CODE = 50000;
    private static final long serialVersionUID = 1L;
    private static final String ERROR_RECORD_ALREADY_EXISTS = "Slog sa datim kljucem vec postoji";
    private static final String ERROR_RECORD_WAS_CHANGED = "Slog je promenjen od strane drugog korisnika. Molim vas, pogledajte njegovu trenutnu vrednost";
    private static final String ERROR_RECORD_WAS_DELETED = "Slog je obrisan od strane drugog korisnika";
    private String tableCode;
    private Map<String, MetaColumn> columns = new LinkedHashMap<>();
    private List<String> pkColumns = new ArrayList<>();
    private Map<String, Integer> columnCodeIndexes = new LinkedHashMap<>();
    private Map<QueryProcedureCallEnum, String> cachedQueriesParameterCalls = new HashMap<>();
    private Map<String, String> nextColumnCodeValues;
    public TableModel(MetaTable metaTable) {
        this.tableCode = metaTable.getCode();

        initMetaTableData(metaTable);
    }
    public TableModel(MetaTable metaTable, Map<String, String> nextColumnCodeValues) {
        this(metaTable);

        if (!nextColumnCodeValues.isEmpty())
            this.nextColumnCodeValues = nextColumnCodeValues;
    }

    private void initMetaTableData(MetaTable metaTable) {
        Vector<String> columnNames = new Vector<>(metaTable.getTotalColumns());
        @SuppressWarnings("unchecked")
        Vector<MetaColumn> metaColumns = (Vector<MetaColumn>) metaTable.cColumns();
        for (int i = 0; i < metaTable.getTotalColumns(); i++) {
            MetaColumn column = metaColumns.get(i);

            columnNames.add(column.getName());

            columns.put(column.getCode(), column);
            columnCodeIndexes.put(column.getCode(), i);

            if (column.isPartOfPK())
                pkColumns.add(column.getCode());

            // TODO filter out columns - hide, etc
        }

        @SuppressWarnings("rawtypes")
        Vector vector = new Vector(columnNames.size());
        vector.setSize(columnNames.size());
        setDataVector(vector, columnNames);
    }

    public String getValue(int selectedRowIndex, String columnCode) {
        return (String) getValueAt(selectedRowIndex, columnCodeIndexes.get(columnCode));
    }

    public void open() throws SQLException {
        if (nextColumnCodeValues == null) {
            fillData(getBasicQuery()
                    + getOrderByQuery());
        } else {
            fillData(getBasicQuery()
                    + " WHERE " + getNextWhereQuery()
                    + getOrderByQuery());
        }
    }

    private void fillData(String query) throws SQLException {
        setRowCount(0);

        ResultSet resultSet;
        Statement stmt;
        if (nextColumnCodeValues != null) {
            stmt = DBConnection.getConnection().prepareStatement(query);
            PreparedStatement pStmt = (PreparedStatement) stmt;

            int count = 1;
            for (String nextValue : nextColumnCodeValues.values()) {
                pStmt.setString(count++, nextValue);
            }

            resultSet = pStmt.executeQuery();
        } else {
            stmt = DBConnection.getConnection().createStatement();
            resultSet = stmt.executeQuery(query);
        }

        String[] rowValues = new String[getColumnCount()];
        while (resultSet.next()) {
            int count = 0;
            for (String columnName : columns.keySet()) {
                rowValues[count++] = resultSet.getString(columnName);
            }

            addRow(rowValues);
        }

        resultSet.close();
        stmt.close();

        fireTableDataChanged();
    }

    public int insertRow(String[] values) throws SQLException {
        int retVal;

        checkRowInsert(values);

        CallableStatement proc = DBConnection.getConnection().prepareCall(
                getCreateProcedureCall());

        setParametersAllCols(proc, values);

        proc.execute();

        proc.close();

        DBConnection.getConnection().commit();

        retVal = sortedInsert(values);
        fireTableDataChanged();

        return retVal;
    }

    public int updateRow(int rowIndex, String[] values) throws SQLException {
        int retVal;

        checkRowUpdate(rowIndex, values);

        CallableStatement proc = DBConnection.getConnection().prepareCall(
                getUpdateProcedureCall());

        // TODO refactor pk column values
        String[] pkValues = new String[pkColumns.size()];
        int i = 0;
        for (String columnCode : pkColumns) {
            pkValues[i++] = (String) getValueAt(rowIndex,
                    columnCodeIndexes.get(columnCode));
        }

        List<String> both = new ArrayList<>(pkValues.length + values.length);
        Collections.addAll(both, pkValues);
        Collections.addAll(both, values);

        setParametersPkAllCols(proc, both.toArray(new String[both.size()]));

        proc.execute();
        proc.close();

        DBConnection.getConnection().commit();

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

        for (String columnCode : pkColumns) {
            int columnIndex = columnCodeIndexes.get(columnCode);

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

        CallableStatement proc = DBConnection.getConnection().prepareCall(
                getDeleteProcedureCall());

        String[] values = new String[pkColumns.size()];
        int count = 0;
        for (String columnCode : pkColumns) {
            int columnIndex = columnCodeIndexes.get(columnCode);
            values[count++] = (String) getValueAt(index, columnIndex);
        }

        setParametersPkCols(proc, values);

        proc.execute();

        proc.close();

        DBConnection.getConnection().commit();

        removeRow(index);
        fireTableDataChanged();
    }

    public int search(String[] values) throws SQLException {
        int retVal = 0;

        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
                getBasicQuery() + getWhereQuery() + getNextWhereQuery() + getOrderByQuery());

        if (nextColumnCodeValues == null)
            setParametersLike(stmt, values);
        else
            setParametersLike(stmt, ArrayUtils.addAll(values,
                    nextColumnCodeValues.values().toArray(new String[nextColumnCodeValues.size()])));

        ResultSet resultSet = stmt.executeQuery();

        if (!resultSet.isBeforeFirst()) {
            // TODO throw exception and remove else
        } else {
            this.setRowCount(0);

            List<String> rowValues = new ArrayList<>();
            while (resultSet.next()) {
                rowValues.clear();

                for (String columnName : columns.keySet()) {
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

    private void checkRowInsert(String[] values) throws SQLException {
        DBConnection.getConnection()
                .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
                getBasicQuery() + getWhereByPksQuery());

        String[] pkValues = new String[pkColumns.size()];
        int i = 0;
        for (String columnCode : pkColumns) {
            pkValues[i++] = values[columnCodeIndexes.get(columnCode)];
        }

        setParametersPkCols(stmt, pkValues);

        ResultSet rset = stmt.executeQuery();

        String errorMessage = null;
        if (rset.isBeforeFirst()) { // if has results
            errorMessage = ERROR_RECORD_ALREADY_EXISTS;
        }

        rset.close();
        stmt.close();

        DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        if (errorMessage != null) {
            DBConnection.getConnection().commit();
            throw new SQLException(errorMessage, "", CUSTOM_ERROR_CODE);
        }
    }

    private void checkRowUpdate(int index, String[] values) throws SQLException {
        DBConnection.getConnection()
                .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        int columnCount = getColumnCount();

        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
                getBasicQuery() + getWhereByPksQuery());

        String[] pkValues = new String[pkColumns.size()];
        int i = 0;
        for (String columnCode : pkColumns) {
            pkValues[i++] = (String) getValueAt(index,
                    columnCodeIndexes.get(columnCode));
        }

        setParametersPkCols(stmt, pkValues);

        ResultSet rset = stmt.executeQuery();

        String errorMessage = null;
        if (rset.isBeforeFirst()) { // if has results
            boolean changed = false;

            // move to result
            rset.next();

            // check if values changed
            for (i = 0; i < columnCount; i++) {
                if (!rset.getString(i + 1).equals(getValueAt(index, i))) {
                    changed = true;
                    break;
                }
            }

            if (changed) {
                for (i = 0; i < columnCount; i++) {
                    setValueAt(rset.getString(i + 1), index, i);
                }

                errorMessage = ERROR_RECORD_WAS_CHANGED;
            }
        } else { // already deleted
            removeRow(index);
            fireTableDataChanged();
            errorMessage = ERROR_RECORD_WAS_DELETED;
        }

        rset.close();
        stmt.close();

        checkRowInsert(values);

        DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        if (errorMessage != null) {
            JOptionPane errorMessageDialog = new JOptionPane(errorMessage,
                    JOptionPane.ERROR_MESSAGE);
            errorMessageDialog.setVisible(true);

            DBConnection.getConnection().commit();
            throw new SQLException(errorMessage, "", CUSTOM_ERROR_CODE);
        }
    }

    private void checkRowDelete(int index) throws SQLException {
        DBConnection.getConnection()
                .setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        int columnCount = getColumnCount();

        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
                getBasicQuery() + getWhereByPksQuery());

        String[] pkValues = new String[pkColumns.size()];
        int i = 0;
        for (String columnCode : pkColumns) {
            pkValues[i++] = (String) getValueAt(index,
                    columnCodeIndexes.get(columnCode));
        }

        setParametersPkCols(stmt, pkValues);

        ResultSet rset = stmt.executeQuery();

        String errorMessage = null;
        if (rset.isBeforeFirst()) { // if has results
            boolean changed = false;

            // move to result
            rset.next();

            // check if values changed
            for (i = 0; i < columnCount; i++) {
                if (!rset.getString(i + 1).equals(getValueAt(index, i))) {
                    changed = true;
                    break;
                }
            }

            if (changed) {
                for (i = 0; i < columnCount; i++) {
                    setValueAt(rset.getString(i + 1), index, i);
                }

                errorMessage = ERROR_RECORD_WAS_CHANGED;
            }
        } else { // already deleted
            removeRow(index);
            fireTableDataChanged();
            errorMessage = ERROR_RECORD_WAS_DELETED;
        }

        rset.close();
        stmt.close();

        DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        if (errorMessage != null) {
            JOptionPane errorMessageDialog = new JOptionPane(errorMessage,
                    JOptionPane.ERROR_MESSAGE);
            errorMessageDialog.setVisible(true);

            DBConnection.getConnection().commit();
            throw new SQLException(errorMessage, "", CUSTOM_ERROR_CODE);
        }
    }

    public String getBasicQuery() {
        String basicQuery = cachedQueriesParameterCalls.get(QueryProcedureCallEnum.BASIC_QUERY);

        if (basicQuery == null) {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT ");

            for (String column : columns.keySet()) {
                sb.append(column);
                sb.append(", ");
            }

            int lastCommaIndex = sb.lastIndexOf(", ");
            sb.delete(lastCommaIndex, lastCommaIndex + 2);
            sb.append(" FROM ");
            sb.append(tableCode);

            basicQuery = sb.toString();
            cachedQueriesParameterCalls.put(QueryProcedureCallEnum.BASIC_QUERY, basicQuery);
        }

        return basicQuery;
    }

    public String getOrderByQuery() {
        String orderByQuery = cachedQueriesParameterCalls
                .get(QueryProcedureCallEnum.ORDER_BY_QUERY);

        if (orderByQuery == null) {
            StringBuilder sb = new StringBuilder();

            sb.append(" ORDER BY ");

            // order by all pks
            for (String column : pkColumns) {
                sb.append(column);
                sb.append(", ");
            }

            int lastCommaIndex = sb.lastIndexOf(", ");
            sb.delete(lastCommaIndex, lastCommaIndex + 2);

            // order by first pk
            // sb.append(pkColumns.keySet().iterator().next());

            orderByQuery = sb.toString();
            cachedQueriesParameterCalls.put(QueryProcedureCallEnum.ORDER_BY_QUERY, orderByQuery);
        }

        return orderByQuery;
    }

    public String getWhereByPksQuery() {
        String whereByPksQuery = cachedQueriesParameterCalls
                .get(QueryProcedureCallEnum.WHERE_PKS_QUERY);

        if (whereByPksQuery == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(" WHERE ");

            for (String column : pkColumns) {
                sb.append(column);
                sb.append("=? AND ");
            }

            int lastAndIndex = sb.lastIndexOf(" AND ");
            sb.delete(lastAndIndex, lastAndIndex + 5);

            whereByPksQuery = sb.toString();

            cachedQueriesParameterCalls
                    .put(QueryProcedureCallEnum.WHERE_PKS_QUERY, whereByPksQuery);
        }

        return whereByPksQuery;
    }

    public String getWhereQuery() {
        String whereQuery = cachedQueriesParameterCalls.get(QueryProcedureCallEnum.WHERE_QUERY);

        if (whereQuery == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(" WHERE ");

            for (String column : columns.keySet()) {
                sb.append(column);
                sb.append(" LIKE ? AND ");
            }

            int lastAndIndex = sb.lastIndexOf(" AND ");
            sb.delete(lastAndIndex, lastAndIndex + 5);

            whereQuery = sb.toString();

            cachedQueriesParameterCalls.put(QueryProcedureCallEnum.WHERE_QUERY, whereQuery);
        }

        return whereQuery;
    }

    public String getNextWhereQuery() {
        String nextWhereQuery = cachedQueriesParameterCalls.get(QueryProcedureCallEnum.NEXT_WHERE_QUERY);

        if (nextWhereQuery == null) {
            if (nextColumnCodeValues == null) {
                nextWhereQuery = "";
                cachedQueriesParameterCalls.put(QueryProcedureCallEnum.WHERE_QUERY, nextWhereQuery);
            } else {
                StringBuilder sb = new StringBuilder();

                for (String column : nextColumnCodeValues.keySet()) {
                    sb.append(column);
                    sb.append(" = ? AND ");
                }

                int lastAndIndex = sb.lastIndexOf(" AND ");
                sb.delete(lastAndIndex, lastAndIndex + 5);

                nextWhereQuery = sb.toString();
                cachedQueriesParameterCalls.put(QueryProcedureCallEnum.NEXT_WHERE_QUERY, nextWhereQuery);
            }
        }

        return nextWhereQuery;
    }

    private String getProcedureCall(String prefix, ProcedureCallStyle pcs) {
        StringBuilder sb = new StringBuilder();

        int columnsCount = 0;

        switch (pcs) {
            case USE_ALL_COLS:
                columnsCount = getColumnCount();
                break;
            case USE_PK_COLS:
                columnsCount = pkColumns.size();
                break;
            case USE_PK_ALL_COLS:
                columnsCount = getColumnCount() + pkColumns.size();
                break;
        }

        sb.append("{ call ");
        sb.append(prefix);
        sb.append("_");
        sb.append(tableCode);
        sb.append("( ");

        for (int i = 0; i < columnsCount; i++) {
            sb.append("?, ");
        }

        int lastCommaIndex = sb.lastIndexOf(", ");
        sb.delete(lastCommaIndex, lastCommaIndex + 2);
        sb.append(" )}");

        return sb.toString();
    }

    public String getCreateProcedureCall() {
        String createProcedureCall = cachedQueriesParameterCalls
                .get(QueryProcedureCallEnum.CREATE_PROCEDURE_CALL);

        // { call c_tableName(?,...,?)}

        if (createProcedureCall == null) {
            createProcedureCall = getProcedureCall("c",
                    ProcedureCallStyle.USE_ALL_COLS);

            cachedQueriesParameterCalls.put(
                    QueryProcedureCallEnum.CREATE_PROCEDURE_CALL,
                    createProcedureCall);
        }

        return createProcedureCall;
    }

    public String getUpdateProcedureCall() {
        String updateProcedureCall = cachedQueriesParameterCalls
                .get(QueryProcedureCallEnum.UPDATE_PROCEDURE_CALL);

        // { call u_tableName(?,...,?)}

        if (updateProcedureCall == null) {
            updateProcedureCall = getProcedureCall("u",
                    ProcedureCallStyle.USE_PK_ALL_COLS);

            cachedQueriesParameterCalls.put(
                    QueryProcedureCallEnum.UPDATE_PROCEDURE_CALL,
                    updateProcedureCall);
        }

        return updateProcedureCall;
    }

    public String getDeleteProcedureCall() {
        String deleteProcedureCall = cachedQueriesParameterCalls
                .get(QueryProcedureCallEnum.DELETE_PROCEDURE_CALL);

        if (deleteProcedureCall == null) {
            deleteProcedureCall = getProcedureCall("d",
                    ProcedureCallStyle.USE_PK_COLS);

            cachedQueriesParameterCalls.put(
                    QueryProcedureCallEnum.DELETE_PROCEDURE_CALL,
                    deleteProcedureCall);
        }

        return deleteProcedureCall;
    }

    private void setParametersLike(PreparedStatement stmt, String[] values)
            throws SQLException {
        Iterator<MetaColumn> it = columns.values().iterator();

        for (int i = 0; i < values.length; i++) {
            MetaColumn column = it.next();

            String columnTypeClass = column.getJClassName();

            switch (columnTypeClass) {
                case "java.lang.String":
                    stmt.setString(i + 1, "%" + values[i] + "%");
            }
        }
    }

    private void setParametersPkCols(PreparedStatement stmt, String[] values)
            throws SQLException {
        Iterator<String> it = pkColumns.iterator();

        for (int i = 0; i < values.length; i++) {
            MetaColumn column = columns.get(it.next());

            String columnTypeClass = column.getJClassName();

            // TODO handle all types
            switch (columnTypeClass) {
                case "java.lang.String":
                    stmt.setString(i + 1, values[i]);
                    break;
                case "java.math.BigDecimal":
                    try {
                        Integer value = Integer.parseInt(values[i]);
                        stmt.setInt(i + 1, value);
                    } catch (Exception e) {
                        stmt.setDouble(i + 1, Double.parseDouble(values[i]));
                    }
                    break;
            }
        }
    }

    private void setParametersAllCols(PreparedStatement stmt, String[] values)
            throws SQLException {
        Iterator<String> it = columns.keySet().iterator();

        for (int i = 0; i < values.length; i++) {
            MetaColumn column = columns.get(it.next());

            String columnTypeClass = column.getJClassName();

            // TODO handle all types
            switch (columnTypeClass) {
                case "java.lang.String":
                    stmt.setString(i + 1, values[i]);
                    break;
                case "java.math.BigDecimal":
                    try {
                        Integer value = Integer.parseInt(values[i]);
                        stmt.setInt(i + 1, value);
                    } catch (Exception e) {
                        stmt.setDouble(i + 1, Double.parseDouble(values[i]));
                    }
                    break;
            }
        }
    }

    private void setParametersPkAllCols(PreparedStatement stmt, String[] values)
            throws SQLException {
        List<String> columnCodes = new ArrayList<>(pkColumns);
        columnCodes.addAll(columns.keySet());

        Iterator<String> it = columnCodes.iterator();

        for (int i = 0; i < values.length; i++) {
            MetaColumn column = columns.get(it.next());

            String columnTypeClass = column.getJClassName();

            // TODO handle all types
            switch (columnTypeClass) {
                case "java.lang.String":
                    stmt.setString(i + 1, values[i]);
                    break;
                case "java.math.BigDecimal":
                    stmt.setDouble(i + 1, Double.parseDouble(values[i]));
                    break;
            }
        }
    }

    public Map<String, String> getNextColumnCodeValues() {
        return nextColumnCodeValues;
    }

    private enum QueryProcedureCallEnum {
        BASIC_QUERY, ORDER_BY_QUERY, WHERE_PKS_QUERY, WHERE_QUERY, NEXT_WHERE_QUERY, UPDATE_PROCEDURE_CALL,
        DELETE_PROCEDURE_CALL, CREATE_PROCEDURE_CALL
    }

    private enum ProcedureCallStyle {
        USE_ALL_COLS, USE_PK_COLS, USE_PK_ALL_COLS
    }


}
