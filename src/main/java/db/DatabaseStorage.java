package db;

import controller.TableController;
import exception.SQLReferenceException;
import factory.ProcedureCallFactory;
import model.db.SchemaColumn;
import model.db.SchemaRow;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DatabaseStorage {

    @Inject
    RowsResultHandler rowsResultHandler;
    @Inject
    StatementDesignator statementDesignator;
    @Inject
    private RowResultHandler rowResultHandler;
    private GenericQueries queries;
    private DatabaseConnection dbConnection;

    @Inject
    public DatabaseStorage(GenericQueries queries, DatabaseConnection dbConnection) {

        this.queries = queries;
        this.dbConnection = dbConnection;
    }

    public List<String[]> select(SchemaRow schemaRow) throws SQLException {
        String query = queries.select(schemaRow);
        System.out.println("Executing query: " + query);
        Statement statement = dbConnection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        Vector<String> columns = schemaRow.getColumnCodes();
        return rowsResultHandler.handle(statement, resultSet, columns);
    }

    public SchemaRow store(SchemaRow object) {
        return object;
    }

    @SuppressWarnings("unchecked")
    public List<String> selectById(SchemaRow schemaRow, List<String> primaryKeyValues) throws SQLException {
        String query = queries.selectById(schemaRow);
        System.out.println("Executing query: " + query);

        PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);

        statementDesignator.designate(schemaRow.getPrimaryKeys(), primaryKeyValues, statement);

        ResultSet resultSet = statement.executeQuery();
        Vector<String> columns = schemaRow.getColumnCodes();
        return rowResultHandler.handle(statement, resultSet, columns);
    }

    @SuppressWarnings("unchecked")
    @Transactional("modifyCheck")
    public void delete(SchemaRow schemaRow, Map<String, String> selectedRowData, TableController tableController) throws SQLException, SQLReferenceException {
        String query = ProcedureCallFactory.getProcedureCall(schemaRow, ProcedureCallFactory.ProcedureCallEnum.DELETE_PROCEDURE_CALL);
        System.out.println("Executing procedure: " + query);

        CallableStatement statement = dbConnection.getConnection().prepareCall(query);
//        ListMultimap<Class, String> mapColumnClazzByValue = ArrayListMultimap.create();
        statementDesignator.designate(schemaRow.getPrimaryKeys(), selectedRowData.values(), statement);

        try {
            statement.execute();
        } catch (SQLException e) {
            // TODO: log ex before throwing new
            e.printStackTrace();
            if (e.getErrorCode() == 547) {
                String message = e.getMessage();
                String[] splited = message.split("table \"");
                throw new SQLReferenceException("Slog ne moze biti obrisan. Reference constraint by " + splited[1].substring(0, splited[1].length() - 2));
            }
        } finally {
            statement.close();
            dbConnection.getConnection().commit();
        }
    }

    @Transactional("modifyCheck")
    public void update(SchemaRow schemaRow, Map<String, String> selectedRowData, TableController tableController) throws SQLException {
        String query = ProcedureCallFactory.getProcedureCall(schemaRow, ProcedureCallFactory.ProcedureCallEnum.UPDATE_PROCEDURE_CALL);
        System.out.println("Executing procedure: " + query);

        List<SchemaColumn> designateValues = new ArrayList<>();
        designateValues.addAll(schemaRow.getPrimaryKeys());
        designateValues.addAll(schemaRow.getColumnsFromBaseTableOnly());

        List<String> updateValues = new ArrayList<>();
        for (SchemaColumn column : schemaRow.getPrimaryKeys()) {
            updateValues.add(selectedRowData.get(column.getCode()));
        }

        for (SchemaColumn column : schemaRow.getColumnsFromBaseTableOnly()) {
            updateValues.add(selectedRowData.get(column.getCode()));
        }
        CallableStatement statement = dbConnection.getConnection().prepareCall(query);
        statementDesignator.designate(designateValues, updateValues, statement);
//
        statement.execute();
        statement.close();
        dbConnection.getConnection().commit();
    }

}
