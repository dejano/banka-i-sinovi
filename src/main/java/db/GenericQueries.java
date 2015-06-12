package db;

import model.db.SchemaColumn;
import model.db.SchemaRow;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class GenericQueries {

    private QueryBuilder queryBuilder;
    private Map<SchemaRow, String> cachedSelectQueries;

    @Inject
    public GenericQueries(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
        cachedSelectQueries = new HashMap<>();
    }

    public String select(SchemaRow model) {
        if (cachedSelectQueries.containsKey(model)) {
            System.out.println("GenericQueries.select " + model.getTableCode() + " Cached");
            return cachedSelectQueries.get(model);
        }
        queryBuilder.select(model.getTableCode(), model.getColumnCodesFromBaseTableOnly());

        for (Map.Entry<SchemaColumn, List<SchemaColumn>> entry : model.getForeignKeys().entrySet()) {
            String foreignTableCode = entry.getValue().get(0).getTableName();
            queryBuilder.with(model.getTableCode(), foreignTableCode, model.getForeignColumnCodes(foreignTableCode), entry.getKey().getCode(), entry.getKey().getCode());
        }
        String query = queryBuilder.from(model.getTableCode()).build();
        cachedSelectQueries.put(model, query);
        return query;
    }

    public String delete(SchemaRow model) {
        String query = queryBuilder.delete().from(model.getTableCode()).where().build();
        return null;
    }

    public String selectById(SchemaRow model) {
        queryBuilder.select(model.getTableCode(), model.getColumnCodesFromBaseTableOnly());

        for (Map.Entry<SchemaColumn, List<SchemaColumn>> entry : model.getForeignKeys().entrySet()) {
            String foreignTableCode = entry.getValue().get(0).getTableName();
            queryBuilder.with(model.getTableCode(), foreignTableCode, model.getForeignColumnCodes(foreignTableCode), entry.getKey().getCode(), entry.getKey().getCode());
        }
        String query = queryBuilder.from(model.getTableCode()).whereByKey(model).build();
        return query;
    }
}
