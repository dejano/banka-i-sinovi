package gui.standard.form.misc;

import java.util.Map;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableQueriesBuilder {

    private QueryBuilder queryBuilder = new QueryBuilder();

    private TableMetaData tableMetaData;
    private Map<String, String> nextColumnCodeValues;

    public TableQueriesBuilder(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    public TableQueriesBuilder(TableMetaData tableMetaData, Map<String, String> nextColumnCodeValues) {
        this.tableMetaData = tableMetaData;
        this.nextColumnCodeValues = nextColumnCodeValues;
    }

    public TableQueriesBuilder getBasicQuery() {
        queryBuilder.select(tableMetaData.getColumns().values())
                .from(tableMetaData.getTableName())
                .leftOuterJoin(tableMetaData.getTableName(), tableMetaData.getLookupJoins().entrySet());

        if (tableMetaData.getCondition() != null)
            queryBuilder.where(tableMetaData.getCondition());

        return this;
    }

    public TableQueriesBuilder getOrderByQuery() {
        queryBuilder.orderBy(tableMetaData.getTableName(), tableMetaData.getPrimaryKeyColumns());
        return this;
    }

    public TableQueriesBuilder getWhereByPksQuery() {
        queryBuilder.where(tableMetaData.getTableName(), tableMetaData.getPrimaryKeyColumns());
        return this;
    }

    public TableQueriesBuilder getWhereLikeQuery() {
        queryBuilder.whereLike(tableMetaData.getTableName(), tableMetaData.getColumnNames());
        return this;
    }

    public TableQueriesBuilder getNextWhereQuery() {
        if (nextColumnCodeValues != null) {
            queryBuilder.where(tableMetaData.getTableName(), nextColumnCodeValues.keySet());
        }

        return this;
    }

    public String build() {
        String ret = queryBuilder.build();

        // TODO replace with map for caching where key is hash
        queryBuilder = new QueryBuilder();

        return ret;
    }
}
