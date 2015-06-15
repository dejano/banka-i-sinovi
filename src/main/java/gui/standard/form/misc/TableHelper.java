package gui.standard.form.misc;

import gui.standard.Column;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableHelper {

    private TableQueriesBuilder tableQueriesBuilder;
    private TableMetaData tableMetaData;

    public TableHelper(TableQueriesBuilder tableQueriesBuilder, TableMetaData tableMetaData) {
        this.tableQueriesBuilder = tableQueriesBuilder;
        this.tableMetaData = tableMetaData;
    }

    public List<Column> getColumnList(Collection<String> keys, String[] values){
        List<Column> ret = new ArrayList<>();
//        String[] clearedVals = new String[keys.size()];


        Iterator<String> it = keys.iterator();
        for (String value : values) {
            String next = it.next();
            System.out.println(next + ":" + value);
            ret.add(new Column(next, value));
        }

        return ret;
    }

    public Map<String, String> createMap(Collection<String> keys, String[] values) {
        Map<String, String> ret = new LinkedHashMap<>();

        Iterator<String> it = keys.iterator();
        for (String value : values) {
            ret.put(it.next(), value);
        }

        return ret;
    }

    public String[] getPkValues(String[] values) {
        String[] pkValues = new String[tableMetaData.getPrimaryKeyColumns().size()];
        int i = 0;
        for (String columnCode : tableMetaData.getPrimaryKeyColumns()) {
            pkValues[i++] = values[tableMetaData.getColumnIndex(columnCode)];
        }

        return pkValues;
    }

    public String[] getPkValues1(String[] baseColumnValues) {
        String[] pkValues = new String[tableMetaData.getPrimaryKeyColumns().size()];
        for (int i = 0; i < tableMetaData.getPrimaryKeyColumns().size(); i++) {
            pkValues[i] = baseColumnValues[i];
        }

        return pkValues;
    }

    public String[] getDbRowByPks(String[] pkValues) throws SQLException {
        String[] ret = null;

        Map<String, String> columnCodeTypes =
                tableMetaData.getColumnCodeTypes(TableMetaData.ColumnGroupsEnum.PRIMARY_KEYS);
        StatementExecutor executor = new StatementExecutor(columnCodeTypes);

        Map<String, String> pkColumnCodeValues = new LinkedHashMap<>();
        int i = 0;
        for (String columnCode : tableMetaData.getPrimaryKeyColumns()) {
            pkColumnCodeValues.put(columnCode, pkValues[i++]);
        }

        List<String[]> results = executor.execute(
                tableQueriesBuilder.getBasicQuery().getWhereByPksQuery().build(),
                tableMetaData.getColumns().keySet(), pkColumnCodeValues);

        if (!results.isEmpty())
            ret = results.get(0);

        return ret;
    }
}
