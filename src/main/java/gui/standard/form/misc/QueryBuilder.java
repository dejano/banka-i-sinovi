package gui.standard.form.misc;

import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejan on 6/8/2015.
 */
public class QueryBuilder {

    private StringBuilder queryBuilder;
    private StringBuilder selectBuilder;
    private StringBuilder relationBuilder;
    private StringBuilder whereBuilder;
    private StringBuilder fromBuilder;

    private boolean select = false;
    private boolean where = false;

    public QueryBuilder() {
        queryBuilder = new StringBuilder();
        selectBuilder = new StringBuilder();
        relationBuilder = new StringBuilder();
        fromBuilder = new StringBuilder();
        whereBuilder = new StringBuilder();
    }

    public QueryBuilder select(MetaTable table) {
        List<String> columnNames = new ArrayList<>();
        for (MetaColumn metaColumn : (List<MetaColumn>) table.cColumns()) {
            columnNames.add(metaColumn.getCode());
        }
        return this.select(table.getCode(), columnNames);
    }

    public QueryBuilder select(String tableName, List<String> columns) {
        if(!select)
            selectBuilder.append("SELECT ");

        select = true;

        for (String column : columns) {
            if (selectBuilder.length() > 0) {
                selectBuilder.append(", ");
            }
            selectBuilder.append(tableName).append(".").append(column);
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
        }

        relationBuilder.append(" JOIN " + foreignTableName + " ON " + baseTableName + "." + baseColumn + " = " + foreignTableName + "." + foreignColumn);
        return this;
    }

    public QueryBuilder where(String tableName, String columnName) {
        if(!where)
            whereBuilder.append(" WHERE ");

        where = true;

        whereBuilder.append(tableName);
        whereBuilder.append(".");
        whereBuilder.append(columnName);
        whereBuilder.append("=? ");

        return this;
    }

    public QueryBuilder whereLike(String tableName, String columnName) {
        if(!where)
            whereBuilder.append(" WHERE ");

        where = true;

        whereBuilder.append(tableName);
        whereBuilder.append(".");
        whereBuilder.append(columnName);
        whereBuilder.append(" LIKE ? ");

        return this;
    }

    public QueryBuilder and(){
        whereBuilder.append(" AND ");

        return this;
    }

    public String build() {
        return queryBuilder.append(selectBuilder).append(fromBuilder).append(relationBuilder).append(whereBuilder).toString();
    }

    @Override
    public String toString() {
        return queryBuilder.toString();
    }

}

