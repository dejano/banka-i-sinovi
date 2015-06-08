package gui.standard.form.misc;

import gui.standard.form.TableJoin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableQueries {

    private Map<QueryEnum, String> cachedQueries = new HashMap<>();

    private TableMetaData tableMetaData;
    private Map<String, String> nextColumnCodeValues;

    public TableQueries(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    public TableQueries(TableMetaData tableMetaData, Map<String, String> nextColumnCodeValues) {
        this.tableMetaData = tableMetaData;
        this.nextColumnCodeValues = nextColumnCodeValues;
    }

    public String getBasicQuery() {
        String basicQuery = cachedQueries.get(QueryEnum.BASIC_QUERY);

        if (basicQuery == null) {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT ");

            for (ColumnMetaData column : tableMetaData.getColumns().values()) {
                sb.append(column.getTableName());
                sb.append(".");
                sb.append(column.getCode());
                sb.append(", ");
            }

            int lastCommaIndex = sb.lastIndexOf(", ");
            sb.delete(lastCommaIndex, lastCommaIndex + 2);

            sb.append(" FROM ");
            sb.append(tableMetaData.getTableName());

            for (String joinColumn : tableMetaData.getLookupJoins().keySet()) {
                TableJoin lookupJoin = tableMetaData.getLookupJoins().get(joinColumn);
                sb.append(" JOIN " + lookupJoin.getTableName() + " ON "
                        + tableMetaData.getTableName() + "." + joinColumn + " = "
                        + lookupJoin.getTableName() + "." + lookupJoin.getJoinColumn());
            }

            basicQuery = sb.toString();
            cachedQueries.put(QueryEnum.BASIC_QUERY, basicQuery);
        }

        return basicQuery;
    }

    public String getOrderByQuery() {
        String orderByQuery = cachedQueries
                .get(QueryEnum.ORDER_BY_QUERY);

        if (orderByQuery == null) {
            StringBuilder sb = new StringBuilder();

            sb.append(" ORDER BY ");

            // order by all pks
            for (String column : tableMetaData.getPrimaryKeyColumns()) {
                sb.append(tableMetaData.getTableName());
                sb.append(".");
                sb.append(column);
                sb.append(", ");
            }

            int lastCommaIndex = sb.lastIndexOf(", ");
            sb.delete(lastCommaIndex, lastCommaIndex + 2);

            // order by first pk
            // sb.append(pkColumns.keySet().iterator().next());

            orderByQuery = sb.toString();
            cachedQueries.put(QueryEnum.ORDER_BY_QUERY, orderByQuery);
        }

        return orderByQuery;
    }

    public String getWhereByPksQuery() {
        String whereByPksQuery = cachedQueries
                .get(QueryEnum.WHERE_PKS_QUERY);

        if (whereByPksQuery == null) {
            QueryBuilder queryBuilder = new QueryBuilder();

            int i = 0;
            for (String column : tableMetaData.getPrimaryKeyColumns()) {
                queryBuilder.where(tableMetaData.getTableName(), column);

                if (i++ != tableMetaData.getPrimaryKeyColumns().size() - 1)
                    queryBuilder.and();
            }

            whereByPksQuery = queryBuilder.build();

            cachedQueries
                    .put(QueryEnum.WHERE_PKS_QUERY, whereByPksQuery);
        }

        return whereByPksQuery;
    }

    public String getWhereQuery() {
        String whereQuery = cachedQueries.get(QueryEnum.WHERE_QUERY);

        if (whereQuery == null) {
            QueryBuilder queryBuilder = new QueryBuilder();

            int i = 0;
            for (String column : tableMetaData.getColumns().keySet()) {
                queryBuilder.whereLike(tableMetaData.getTableName(), column);

                if (i++ != tableMetaData.getColumns().size() - 1)
                    queryBuilder.and();
            }

            whereQuery = queryBuilder.build();

            cachedQueries.put(QueryEnum.WHERE_QUERY, whereQuery);
        }

        return whereQuery;
    }

    public String getNextWhereQuery() {
        String nextWhereQuery = cachedQueries.get(QueryEnum.NEXT_WHERE_QUERY);

        if (nextWhereQuery == null) {
            if (nextColumnCodeValues == null) {
                nextWhereQuery = "";
                cachedQueries.put(QueryEnum.WHERE_QUERY, nextWhereQuery);
            } else {
                QueryBuilder queryBuilder = new QueryBuilder();

                int i = 0;
                for (String column : nextColumnCodeValues.keySet()) {
                    queryBuilder.where(tableMetaData.getTableName(), column);

                    if (i++ != nextColumnCodeValues.size() - 1)
                        queryBuilder.and();
                }

                nextWhereQuery = queryBuilder.build();
                cachedQueries.put(QueryEnum.NEXT_WHERE_QUERY, nextWhereQuery);
            }
        }

        return nextWhereQuery;
    }

    private enum QueryEnum {
        BASIC_QUERY, ORDER_BY_QUERY, WHERE_PKS_QUERY, WHERE_QUERY, NEXT_WHERE_QUERY
    }
}
