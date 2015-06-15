package gui.standard.form.misc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by dejan on 6/8/2015.
 */
public class QueryBuilder {

    private StringBuilder queryBuilder;
    private StringBuilder selectBuilder;
    private StringBuilder relationBuilder;
    private StringBuilder whereBuilder;
    private StringBuilder fromBuilder;
    private StringBuilder orderByBuilder;

    private boolean select = false;
    private boolean where = false;
    private boolean orderBy = false;

    private static final String SELECT = "SELECT ";
    private static final String FROM = " FROM ";

    private static final String JOIN = " JOIN ";
    private static final String LEFT_OUTER_JOIN = " LEFT OUTER JOIN ";
    private static final String ON = " ON ";

    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String EQUAL = " = ";
    private static final String EQUAL_QM = "=?";
    private static final String LIKE = " LIKE ?";

    private static final String ORDER_BY = " ORDER BY ";

    private static final char DOT = '.';
    private static final String COMMA = ", ";

    public QueryBuilder() {
        queryBuilder = new StringBuilder();
        selectBuilder = new StringBuilder();
        relationBuilder = new StringBuilder();
        fromBuilder = new StringBuilder();
        whereBuilder = new StringBuilder();
        orderByBuilder = new StringBuilder();
    }

    public QueryBuilder select(Collection<ColumnMetaData> columns) {
        if (!select) {
            selectBuilder.append(SELECT);
            select = true;
        }

        for (ColumnMetaData column : columns) {
            selectBuilder.append(column.getTableName()).append(DOT).append(column.getCode()).append(COMMA);
        }

        removeLastComma(selectBuilder);

        return this;
    }

    public QueryBuilder from(String table) {
        fromBuilder.append(FROM).append(table);
        return this;
    }

    public QueryBuilder join(String baseTableName, String foreignTableName,
                             String baseColumn, String foreignColumn) {
        relationBuilder.append(JOIN).append(foreignTableName).append(ON)
                .append(baseTableName).append(DOT).append(baseColumn)
                .append(" = ").append(foreignTableName).append(".").append(foreignColumn);

        return this;
    }

    public QueryBuilder leftOuterJoin(String baseTableName, Collection<Map.Entry<String, TableJoin>> tableJoins) {
        for (Map.Entry<String, TableJoin> entry : tableJoins) {
            relationBuilder.append(LEFT_OUTER_JOIN)
                    .append(entry.getValue().getTableName())
                    .append(ON)
                    .append(baseTableName).append(DOT).append(entry.getKey())
                    .append(EQUAL)
                    .append(entry.getValue().getTableName()).append(DOT).append(entry.getValue().getJoinColumn());
        }

        return this;
    }

    public QueryBuilder where(String condition) {
        if (!where) {
            whereBuilder.append(WHERE);
            where = true;
        }

        whereBuilder.append(condition);

        return this;
    }

    public QueryBuilder where(String tableName, String columnName) {
        if (!where) {
            whereBuilder.append(WHERE);
            where = true;
        } else {
            and();
        }

        whereBuilder.append(tableName).append(DOT).append(columnName).append(EQUAL_QM);

        return this;
    }

    public QueryBuilder where(String tableName, Collection<String> columnNames) {
        if (!where) {
            whereBuilder.append(WHERE);
            where = true;
        }

        for (String columnName : columnNames) {
            whereBuilder.append(tableName).append(DOT).append(columnName).append(EQUAL_QM);
            and();
        }

        removeLastAnd();

        return this;
    }

    public QueryBuilder whereLike(String tableName, List<String> columnNames) {
        if (!where) {
            whereBuilder.append(WHERE);
            where = true;
        }

        for (String columnName : columnNames) {
            whereBuilder.append(tableName).append(DOT).append(columnName).append(LIKE);
            and();
        }

        removeLastAnd();

        return this;
    }

    public QueryBuilder and() {
        whereBuilder.append(AND);

        return this;
    }

    public QueryBuilder orderBy(String tableName, List<String> columnNames) {
        if (!orderBy) {
            orderByBuilder.append(ORDER_BY);
            orderBy = true;
        }

        for (String columnName : columnNames) {
            orderByBuilder.append(tableName).append(DOT).append(columnName).append(COMMA);
        }

        removeLastComma(orderByBuilder);

        return this;
    }

    public String build() {
        return queryBuilder.append(selectBuilder).append(fromBuilder)
                .append(relationBuilder).append(whereBuilder).append(orderByBuilder).toString();
    }

    private void removeLastAnd() {
        int lastAndIndex = whereBuilder.lastIndexOf(AND);
        whereBuilder.replace(lastAndIndex, lastAndIndex + 5, "");
    }

    private void removeLastComma(StringBuilder sb) {
        int lastCommaIndex = sb.lastIndexOf(COMMA);
        sb.replace(lastCommaIndex, lastCommaIndex + 2, "");
    }

    @Override
    public String toString() {
        return queryBuilder.toString();
    }

}

