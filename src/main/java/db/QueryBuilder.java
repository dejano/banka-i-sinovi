package db;

import model.db.SchemaColumn;
import model.db.SchemaRow;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class QueryBuilder {

    public DatabaseConnection databaseConnection;
    private StringBuilder queryBuilder;
    private StringBuilder selectBuilder;
    private String preKeyword;
    private StringBuilder relationBuilder;
    private StringBuilder whereBuilder;
    private List<String> columns;
    private StringBuilder fromBuilder;

    @Inject
    public QueryBuilder(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        queryBuilder = new StringBuilder();
        selectBuilder = new StringBuilder();
        relationBuilder = new StringBuilder();
        fromBuilder = new StringBuilder();
        whereBuilder = new StringBuilder();
    }

    public QueryBuilder select(String tableName, Vector<String> columns) {
        preKeyword = "SELECT ";
        this.columns = new ArrayList<>();
        for (String column : columns) {
            if (selectBuilder.length() > 0) {
                selectBuilder.append(", ");
            }
            selectBuilder.append(tableName).append(".").append(column);
            this.columns.add(column);
        }
        return this;
    }

    public QueryBuilder select(MetaTable table) {
        List<String> columnNames = new ArrayList<>();
        for (MetaColumn metaColumn : (List<MetaColumn>) table.cColumns()) {
            columnNames.add(metaColumn.getCode());
        }
        return this.select(table.getCode(), columnNames);
    }

    public QueryBuilder select(String tableName, List<String> columns) {
        this.columns = new ArrayList<>();
        for (String column : columns) {
            if (selectBuilder.length() > 0) {
                selectBuilder.append(", ");
            }
            selectBuilder.append(tableName).append(".").append(column);
            this.columns.add(column);
        }
        return this;
    }

    public QueryBuilder from(String from) {
        fromBuilder.append(" FROM " + from);
        return this;
    }

    public QueryBuilder with(String baseTableName, String foreignTableName, List<String> columns, String baseColumn, String foreignColumn) {
        for (String column : columns) {
            if (selectBuilder.length() > 0) {
                selectBuilder.append(", ");
            }
            selectBuilder.append(column).append(" AS \"" + column + "\"");
            this.columns.add(this.columns.indexOf(baseColumn) + 1, column);
        }

        relationBuilder.append(" JOIN " + foreignTableName + " ON " + baseTableName + "." + baseColumn + " = " + foreignTableName + "." + foreignColumn);
        return this;
    }

    public QueryBuilder delete() {
        preKeyword = "DELETE ";
        return this;
    }

    public QueryBuilder where() {
        return this;
    }

    public QueryBuilder andWhere() {
        return this;
    }

    public QueryBuilder orWhere() {
        return this;
    }

    public QueryBuilder whereByKey(SchemaRow model) {
        for (SchemaColumn column : model.getPrimaryKeys()) {
            if (whereBuilder.length() > 0) {
                whereBuilder.append(" AND ");
            }
            whereBuilder.append(model.getTableCode() +"."+column.getCode() + " = ?");
        }
        whereBuilder.insert(0, " WHERE ");
        return this;
    }

    private void rowCallback(Statement statement, ResultSet resultSet, List<String[]> result) throws SQLException {
        String[] rowValues = new String[columns.size()];
        while (resultSet.next()) {
            int count = 0;
            for (String columnName : columns) {
                rowValues[count++] = resultSet.getString(columnName);
            }
            result.add(rowValues);
            rowValues = new String[columns.size()];
        }

        resultSet.close();
        statement.close();
    }

    public String build() {
        queryBuilder.insert(0, preKeyword);
        String result = queryBuilder.append(selectBuilder).append(fromBuilder).append(relationBuilder).append(whereBuilder).toString();
        clearBuilders();
        return result;
    }

    private void clearBuilders() {
        queryBuilder.setLength(0);
        selectBuilder.setLength(0);
        fromBuilder.setLength(0);
        relationBuilder.setLength(0);
        whereBuilder.setLength(0);
    }

    @Override
    public String toString() {
        return queryBuilder.toString();
    }
}
