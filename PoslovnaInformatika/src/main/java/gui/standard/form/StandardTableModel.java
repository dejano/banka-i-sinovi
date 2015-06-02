package gui.standard.form;

import gui.standard.SortUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import meta.MosquitoSingletone;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;
import database.DBConnection;

public class StandardTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	private String tableCode;
	private Map<String, MetaColumn> columns = new LinkedHashMap<String, MetaColumn>();
	private Map<String, MetaColumn> pkColumns = new LinkedHashMap<String, MetaColumn>();
	private Map<String, Integer> columnCodeIndexes = new LinkedHashMap<String, Integer>();

	private enum QueryProcedureCallEnum {
		BASIC_QUERY, ORDER_BY_QUERY, WHERE_PKS_QUERY, WHERE_QUERY, UPDATE_PROCEDURE_CALL, DELETE_PROCEDURE_CALL, CREATE_PROCEDURE_CALL
	}

	private Map<QueryProcedureCallEnum, String> cachedQueriesParameterCalls = new HashMap<QueryProcedureCallEnum, String>();

	public StandardTableModel(MetaTable metaTable) {
		this.tableCode = metaTable.getCode();

		@SuppressWarnings("unchecked")
		Vector<String> columnNames = new Vector<String>(
				metaTable.getTotalColumns());
		Vector<MetaColumn> metaColumns = (Vector<MetaColumn>) metaTable
				.cColumns();
		for (int i = 0; i < metaTable.getTotalColumns(); i++) {
			MetaColumn column = metaColumns.get(i);

			columnNames.add(column.getName());

			columns.put(column.getCode(), column);
			columnCodeIndexes.put(column.getCode(), i);
			if (column.isPartOfPK())
				pkColumns.put(column.getCode(), column);

			// TODO filter out columns - hide, etc
		}

		@SuppressWarnings("rawtypes")
		Vector vector = new Vector(columnNames.size());
		vector.setSize(columnNames.size());
		setDataVector(vector, columnNames);
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

			for (String column : pkColumns.keySet()) {
				sb.append(column);
				sb.append(", ");
			}

			int lastCommaIndex = sb.lastIndexOf(", ");
			sb.delete(lastCommaIndex, lastCommaIndex + 2);

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

			for (String column : pkColumns.keySet()) {
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

	public String getCreateProcedureCall() {
		String createProcedureCall = cachedQueriesParameterCalls
				.get(QueryProcedureCallEnum.CREATE_PROCEDURE_CALL);

		// { call c_tableName(?,...,?)}

		if (createProcedureCall == null) {
			createProcedureCall = getProcedureCall("c", false);

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
			updateProcedureCall = getProcedureCall("u", false);

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
			deleteProcedureCall = getProcedureCall("d", true);

			cachedQueriesParameterCalls.put(
					QueryProcedureCallEnum.DELETE_PROCEDURE_CALL,
					deleteProcedureCall);
		}

		return deleteProcedureCall;
	}

	private String getProcedureCall(String prefix, boolean useOnlyPkColumns) {
		StringBuilder sb = new StringBuilder();

		int columnsCount;
		if (useOnlyPkColumns)
			columnsCount = pkColumns.size();
		else
			columnsCount = columns.size();

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

	public void open() throws SQLException {
//		fillData(getBasicQuery()
//		// + whereStmt
//				+ getOrderByQuery());
	}

	private void fillData(String sql) throws SQLException {
		setRowCount(0);

		Statement stmt = DBConnection.getConnection().createStatement();
		ResultSet rset = stmt.executeQuery(sql);

		List<String> rowValues = new ArrayList<String>();
		while (rset.next()) {
			rowValues.clear();

			for (String columnName : columns.keySet()) {
				rset.getString(columnName);
			}

			addRow(rowValues.toArray());
		}

		rset.close();
		stmt.close();

		fireTableDataChanged();
	}

	public void deleteRow(int index) throws SQLException {
		boolean executed = false;

		checkRow(index);

		try {
			CallableStatement proc = DBConnection.getConnection().prepareCall(
					getDeleteProcedureCall());

			String[] values = new String[pkColumns.size()];
			int count = 0;
			for (String columnCode : pkColumns.keySet()) {
				int columnIndex = columnCodeIndexes.get(columnCode);
				values[count++] = (String) getValueAt(index, columnIndex);
			}

			setParameters(proc, values, true);

			executed = proc.execute();

			proc.close();

			DBConnection.getConnection().commit();

			if (executed) {
				removeRow(index);
				fireTableDataChanged();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int sortedInsert(String[] values) {
		int left = 0;
		int right = getRowCount() - 1;
		int mid = (left + right) / 2;

		// TODO
		// while (left <= right) {
		// mid = (left + right) / 2;
		// String aSifra = (String) getValueAt(mid, 0);
		// if (SortUtils.getLatCyrCollator().compare(sifra, aSifra) > 0)
		// left = mid + 1;
		// else if (SortUtils.getLatCyrCollator().compare(sifra, aSifra) < 0)
		// right = mid - 1;
		// else
		// break;
		// }
		//
		// insertRow(left, new String[] { sifra, naziv });

		return left;
	}

	public int insertRow(String[] values) throws SQLException {
		int retVal = 0;
		boolean executed = false;

		try {
			CallableStatement proc = DBConnection.getConnection().prepareCall(
					getCreateProcedureCall());

			setParameters(proc, values, false);

			executed = proc.execute();

			proc.close();

			DBConnection.getConnection().commit();

			if (executed) {
				retVal = sortedInsert(values);
				fireTableDataChanged();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retVal;
	}
	
	public int updateRow(String[] values) throws SQLException {
		int retVal = 0;
		boolean executed = false;

		try {
			CallableStatement proc = DBConnection.getConnection().prepareCall(
					getUpdateProcedureCall());

			setParameters(proc, values, false);

			executed = proc.execute();

			proc.close();

			DBConnection.getConnection().commit();

			// TODO update table
			if (executed) {
				retVal = sortedInsert(values);
				fireTableDataChanged();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	public int search(String[] values) throws SQLException {
		int retVal = 0;

		PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
				getBasicQuery() + getWhereQuery());

		setParametersLike(stmt, values);

		// TODO show results in table
		
		// int rowsAffected = stmt.executeQuery();
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
		
		PreparedStatement stmt = DBConnection.getConnection()
				.prepareStatement(getBasicQuery() + getWhereByPksQuery());
		
		String[] values = new String[getColumnCount()];
		for (int i = 0; i < getColumnCount(); i++){
			values[i] = (String) getValueAt(index, i);
		}

		setParameters(stmt, values, false);
		
		ResultSet rset = stmt.executeQuery();

		String sifraDr = "", naziv = "";
		Boolean exists = false;
		String errorMsg = "";
		while (rset.next()) {
			sifraDr = rset.getString("DR_SIFRA").trim();
			naziv = rset.getString("DR_NAZIV");
			exists = true;
		}

		if (!exists) {
			removeRow(index);
			fireTableDataChanged();
			errorMsg = ERROR_RECORD_WAS_DELETED;
		} else if ((SortUtils.getLatCyrCollator().compare(sifraDr, // TODO wtf?
				((String) getValueAt(index, 0)).trim()) != 0)
				|| (SortUtils.getLatCyrCollator().compare(naziv,
						(String) getValueAt(index, 1)) != 0)) {
			setValueAt(sifraDr, index, 0);
			setValueAt(naziv, index, 1);
			fireTableDataChanged();
			errorMsg = ERROR_RECORD_WAS_CHANGED;
		}

		rset.close();
		stmt.close();

		DBConnection.getConnection().setTransactionIsolation(
				Connection.TRANSACTION_READ_COMMITTED);

		if (errorMsg != "") {
			JOptionPane errorMessageDialog = new JOptionPane(errorMsg,
					JOptionPane.ERROR_MESSAGE);
			errorMessageDialog.setVisible(true);

			DBConnection.getConnection().commit();
			throw new SQLException(errorMsg, "", CUSTOM_ERROR_CODE);
		}
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

	private void setParameters(PreparedStatement stmt, String[] values, boolean useOnlyPkColumns)
			throws SQLException {
		Iterator<MetaColumn> it = (useOnlyPkColumns ? pkColumns : columns).values().iterator();

		for (int i = 0; i < values.length; i++) {
			MetaColumn column = it.next();

			String columnTypeClass = column.getJClassName();

			// TODO handle all types
			switch (columnTypeClass) {
			case "java.lang.String":
				stmt.setString(i + 1, values[i]);
				break;
			}
		}
	}
	

	public static void main(String[] args) {
		MosquitoSingletone mosq = MosquitoSingletone.getInstance();

		StandardTableModel stm = new StandardTableModel(
				mosq.getMetaTable("VIDEOTEKA"));

		System.out.println(stm.getBasicQuery());
		System.out.println(stm.getOrderByQuery());
		System.out.println(stm.getDeleteProcedureCall());
		System.out.println(stm.getCreateProcedureCall());
		System.out.println(stm.getUpdateProcedureCall());
		System.out.println(stm.getWhereByPksQuery());
		System.out.println(stm.getWhereQuery());
	}
}
