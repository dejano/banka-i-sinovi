package gui.standard.form;

import gui.standard.SortUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import meta.MosquitoSingletone;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;
import database.DBConnection;

public class TableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private String tableCode;
	private Map<String, MetaColumn> columns = new LinkedHashMap<String, MetaColumn>();
	private List<String> pkColumns = new ArrayList<String>();
	private Map<String, Integer> columnCodeIndexes = new LinkedHashMap<String, Integer>();

	private enum QueryProcedureCallEnum {
		BASIC_QUERY, ORDER_BY_QUERY, WHERE_PKS_QUERY, WHERE_QUERY, UPDATE_PROCEDURE_CALL, DELETE_PROCEDURE_CALL, CREATE_PROCEDURE_CALL
	}

	private Map<QueryProcedureCallEnum, String> cachedQueriesParameterCalls = new HashMap<QueryProcedureCallEnum, String>();

	public TableModel(MetaTable metaTable) {
		this.tableCode = metaTable.getCode();

		Vector<String> columnNames = new Vector<String>(
				metaTable.getTotalColumns());
		@SuppressWarnings("unchecked")
		Vector<MetaColumn> metaColumns = (Vector<MetaColumn>) metaTable
				.cColumns();
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

	public void open() throws SQLException {
		fillData(getBasicQuery()
		// + whereStmt
				+ getOrderByQuery());
	}

	private void fillData(String sql) throws SQLException {
		setRowCount(0);

		Statement stmt = DBConnection.getConnection().createStatement();
		ResultSet rset = stmt.executeQuery(sql);

		String[] rowValues = new String[getColumnCount()];
		while (rset.next()) {
			int count = 0;
			for (String columnName : columns.keySet()) {
				rowValues[count++] = rset.getString(columnName);
			}

			addRow(rowValues);
		}

		rset.close();
		stmt.close();

		fireTableDataChanged();
	}

	public int insertRow(String[] values) throws SQLException {
		int retVal = 0;

		try {
			CallableStatement proc = DBConnection.getConnection().prepareCall(
					getCreateProcedureCall());

			setParametersAllCols(proc, values);

			proc.execute();

			proc.close();

			DBConnection.getConnection().commit();

			retVal = sortedInsert(values);
			fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	public int updateRow(int rowIndex, String[] values) throws SQLException {
		int retVal = 0;

		try {
			CallableStatement proc = DBConnection.getConnection().prepareCall(
					getUpdateProcedureCall());

			String[] pkValues = new String[pkColumns.size()];
			int i = 0;
			for (String columnCode : pkColumns) {
				pkValues[i++] = (String) getValueAt(rowIndex,
						columnCodeIndexes.get(columnCode));
			}
			
			List<String> both = new ArrayList<String>(pkValues.length + values.length);
		    Collections.addAll(both, pkValues);
		    Collections.addAll(both, values);
			
			setParametersPkAllCols(proc, both.toArray(new String[both.size()]));

			proc.execute();

			proc.close();

			DBConnection.getConnection().commit();

			removeRow(rowIndex);
			retVal = sortedInsert(values);
			fireTableDataChanged();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	private int sortedInsert(String[] values) {
		int left = 0;
		int right = getRowCount() - 1;
		int mid = (left + right) / 2;

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

			int compareResult = SortUtils.getLatCyrCollator().compare(value,
					tableValue);

			if (compareResult != 0) {
				ret = compareResult;
				break;
			}
		}

		return ret;
	}

	public void deleteRow(int index) throws SQLException {
		checkRow(index);

		try {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int search(String[] values) throws SQLException {
		int retVal = 0;

		PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
				getBasicQuery() + getWhereQuery() + getOrderByQuery());

		setParametersLike(stmt, values);

		ResultSet rset = stmt.executeQuery();

		if (!rset.isBeforeFirst()) {
			// TODO throw exception and remove else
		} else {
			this.setRowCount(0);

			List<String> rowValues = new ArrayList<String>();
			while (rset.next()) {
				rowValues.clear();

				for (String columnName : columns.keySet()) {
					rset.getString(columnName);
				}

				addRow(rowValues.toArray());
			}
		}

		rset.close();
		stmt.close();

		DBConnection.getConnection().commit();

		// if (rowsAffected > 0) {
		// retVal = sortedInsert(values);
		// fireTableDataChanged();
		// }

		return retVal;
	}

	private static final int CUSTOM_ERROR_CODE = 50000;
	private static final String ERROR_RECORD_WAS_CHANGED = "Slog je promenjen od strane drugog korisnika. Molim vas, pogledajte njegovu trenutnu vrednost";
	private static final String ERROR_RECORD_WAS_DELETED = "Slog je obrisan od strane drugog korisnika";

	private void checkRow(int index) throws SQLException {
		DBConnection.getConnection().setTransactionIsolation(
				Connection.TRANSACTION_REPEATABLE_READ);

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
				if (!rset.getString(i + 1)
						.equals((String) getValueAt(index, i))) {
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

		DBConnection.getConnection().setTransactionIsolation(
				Connection.TRANSACTION_READ_COMMITTED);

		if (errorMessage != null) {
			JOptionPane errorMessageDialog = new JOptionPane(errorMessage,
					JOptionPane.ERROR_MESSAGE);
			errorMessageDialog.setVisible(true);

			DBConnection.getConnection().commit();
			throw new SQLException(errorMessage, "", CUSTOM_ERROR_CODE);
		}
	}

	public String getBasicQuery() {
		String basicQuery = cachedQueriesParameterCalls
				.get(QueryProcedureCallEnum.BASIC_QUERY);

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
			cachedQueriesParameterCalls.put(QueryProcedureCallEnum.BASIC_QUERY,
					basicQuery);
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
			cachedQueriesParameterCalls.put(
					QueryProcedureCallEnum.ORDER_BY_QUERY, orderByQuery);
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

			cachedQueriesParameterCalls.put(
					QueryProcedureCallEnum.WHERE_PKS_QUERY, whereByPksQuery);
		}

		return whereByPksQuery;
	}

	public String getWhereQuery() {
		String whereQuery = cachedQueriesParameterCalls
				.get(QueryProcedureCallEnum.WHERE_QUERY);

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

			cachedQueriesParameterCalls.put(QueryProcedureCallEnum.WHERE_QUERY,
					whereQuery);
		}

		return whereQuery;
	}

	private enum ProcedureCallStyle {
		USE_ALL_COLS, USE_PK_COLS, USE_PK_ALL_COLS
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
		List<String> columnCodes = new ArrayList<String>(pkColumns);
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

	public static void main(String[] args) {
		MosquitoSingletone mosq = MosquitoSingletone.getInstance();

		TableModel stm = new TableModel(mosq.getMetaTable("UCESNICI_FILMA"));

		for (String string : stm.columns.keySet()) {
			System.out.println(stm.columns.get(string).getJClassName());
		}

		System.out.println(stm.getBasicQuery());
		System.out.println(stm.getOrderByQuery());
		System.out.println(stm.getDeleteProcedureCall());
		System.out.println(stm.getCreateProcedureCall());
		System.out.println(stm.getUpdateProcedureCall());
		System.out.println(stm.getWhereByPksQuery());
		System.out.println(stm.getWhereQuery());
	}
}
