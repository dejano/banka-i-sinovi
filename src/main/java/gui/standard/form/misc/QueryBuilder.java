package gui.standard.form.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by dejan on 6/8/2015.
 */
public class QueryBuilder {

    private Query query = new Query();

    private StringBuilder queryBuilder;
    private StringBuilder selectBuilder;
    private StringBuilder relationBuilder;
    private StringBuilder whereBuilder;
    private StringBuilder fromBuilder;
    private StringBuilder orderByBuilder;

    private boolean select = false;
    private boolean where = false;
    private boolean orderBy = false;

    private static final String SELECT = "SELECT DISTINCT ";
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

    public QueryBuilder select(String tableName, List<String> columns) {
        if (!select) {
            selectBuilder.append(SELECT);
            select = true;
        }

        for (String columnCode : columns) {
            selectBuilder.append(tableName).append(DOT).append(columnCode).append(COMMA);
        }

        removeLastComma(selectBuilder);

        return this;
    }

    public QueryBuilder select(Collection<ColumnData> columns) {
        if (!select) {
            selectBuilder.append(SELECT);
            select = true;
        }

        for (ColumnData column : columns) {
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

    public QueryBuilder leftOuterJoin(String baseTableName, List<TableJoin> tableJoins) {
        for (TableJoin tableJoin : tableJoins) {
            relationBuilder.append(LEFT_OUTER_JOIN)
                    .append(tableJoin.getTableName())
                    .append(ON)
                    .append(baseTableName).append(DOT).append(tableJoin.getFromColumn())
                    .append(EQUAL)
                    .append(tableJoin.getTableName()).append(DOT).append(tableJoin.getToColumn());
        }

        return this;
    }

    public QueryBuilder where(String condition) {
        if (!where) {
            whereBuilder.append(WHERE);
            where = true;
        } else {
            and();
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
        query.getWheres().add(Query.WhereTypesEnum.EQUALS);

        return this;
    }

    public QueryBuilder where(String tableName, Collection<String> columnNames) {
        if (columnNames.size() > 0) {
            if (!where) {
                whereBuilder.append(WHERE);
                where = true;
            } else {
                and();
            }

            for (String columnName : columnNames) {
                whereBuilder.append(tableName).append(DOT).append(columnName).append(EQUAL_QM);
                query.getWheres().add(Query.WhereTypesEnum.EQUALS);
                and();
            }

            removeLastAnd();
        }

        return this;
    }

    public QueryBuilder whereLike(Collection<String> tableDotCodes) {
        if (tableDotCodes.size() > 0) {
            if (!where) {
                whereBuilder.append(WHERE);
                where = true;
            } else {
                and();
            }

            for (String tableDotCode : tableDotCodes) {
                whereBuilder.append(tableDotCode).append(LIKE);
                query.getWheres().add(Query.WhereTypesEnum.LIKE);
                and();
            }

            removeLastAnd();
        }

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

    public QueryBuilder orderBy(String customOrderBy) {
        if (!orderBy) {
            orderByBuilder.append(ORDER_BY);
            orderBy = true;
        }

        orderByBuilder.append(customOrderBy);

        return this;
    }

    public Query build() {
        query.setQuery(queryBuilder.append(selectBuilder).append(fromBuilder)
                .append(relationBuilder).append(whereBuilder).append(orderByBuilder).toString());
        return query;
    }

    private void removeLastAnd() {
        int lastAndIndex = whereBuilder.lastIndexOf(AND);
        if (lastAndIndex != -1)
            whereBuilder.replace(lastAndIndex, lastAndIndex + 5, "");
    }

    private void removeLastComma(StringBuilder sb) {
        int lastCommaIndex = sb.lastIndexOf(COMMA);
        if (lastCommaIndex != -1)
            sb.replace(lastCommaIndex, lastCommaIndex + 2, "");
    }

    @Override
    public String toString() {
        return queryBuilder.toString();
    }

    public static class Query {
        private String query;
        private List<WhereTypesEnum> wheres = new ArrayList<>();

        public Query() {
        }

        public Query(String query, WhereTypesEnum... wheres) {
            this.query = query;
            for (WhereTypesEnum where : wheres) {
                this.wheres.add(where);
            }
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public List<WhereTypesEnum> getWheres() {
            return wheres;
        }

        public void setWheres(List<WhereTypesEnum> wheres) {
            this.wheres = wheres;
        }

        public enum WhereTypesEnum {
            EQUALS, LIKE
        }
    }
}

