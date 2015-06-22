package gui.standard.form.misc;

import meta.LookupMetaData;
import meta.Zoom;

import java.util.List;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.*;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableQueriesBuilder {

    private QueryBuilder queryBuilder = new QueryBuilder();

    private FormData formData;

    public TableQueriesBuilder(FormData formData) {
        this.formData = formData;
    }

    public TableQueriesBuilder getBasicQuery() {
        queryBuilder.select(formData.getColumnsMap(ALL).values())
                .from(formData.getTableName())
                .leftOuterJoin(formData.getTableName(), formData.getLookupJoins());

        if (formData.getCondition() != null)
            queryBuilder.where(formData.getCondition());

        return this;
    }

    public TableQueriesBuilder getWhereByPksQuery() {
        queryBuilder.where(formData.getTableName(), formData.getColumnCodes(PRIMARY_KEYS));
        return this;
    }

    public TableQueriesBuilder getWhereLikeQuery() {
        queryBuilder.whereLike(formData.getTableDotCodes(ALL));
        return this;
    }

    public TableQueriesBuilder getNextWhereQuery() {
        List<String> nextColumnCodes = formData.getColumnCodes(NEXT);

        if (!nextColumnCodes.isEmpty()) {
            queryBuilder.where(formData.getTableName(), nextColumnCodes);
        }

        return this;
    }

    public TableQueriesBuilder getOrderByQuery() {
        if (formData.getCustomOrderBy() != null)
            queryBuilder.orderBy(formData.getCustomOrderBy());
        else
            queryBuilder.orderBy(formData.getTableName(), formData.getColumnCodes(PRIMARY_KEYS));

        return this;
    }

    public TableQueriesBuilder getLookupsQuery(Zoom zoom, LookupMetaData lookup) {
        queryBuilder.select(lookup.getTable(), lookup.getColumnCodes())
                .from(lookup.getTable())
                .where(zoom.getTableCode(), zoom.getToColumnCodes());

        return this;
    }

    public QueryBuilder.Query build() {
        QueryBuilder.Query ret = queryBuilder.build();

        // TODO replace with mapBoolean for caching where key is hash
        queryBuilder = new QueryBuilder();

        return ret;
    }
}
