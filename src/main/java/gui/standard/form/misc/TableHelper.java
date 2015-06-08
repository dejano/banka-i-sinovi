package gui.standard.form.misc;

import database.DBConnection;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableHelper {

    private TableQueries tableQueries;
    private TableMetaData tableMetaData;

    public TableHelper(TableQueries tableQueries, TableMetaData tableMetaData) {
        this.tableQueries = tableQueries;
        this.tableMetaData = tableMetaData;
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

    public String[] getDbRowByPks(String[] pkValues) throws SQLException {
        String[] ret = null;

        Map<String, String> columnCodeTypes =
                tableMetaData.getColumnCodeTypes(TableMetaData.ColumnGroupsEnum.PRIMARY_KEYS);
        StatementExecutor executor = new StatementExecutor(columnCodeTypes);

        Map<String, String> pkColumnCodeValues = new LinkedHashMap<>();
        for (String columnCode : tableMetaData.getPrimaryKeyColumns()) {
            pkColumnCodeValues.put(columnCode, pkValues[tableMetaData.getColumnIndex(columnCode)]);
        }

        List<String[]> results = executor.execute(
                tableQueries.getBasicQuery() + tableQueries.getWhereByPksQuery(),
                tableMetaData.getColumns().keySet(), pkColumnCodeValues);

        if (!results.isEmpty())
            ret = results.get(0);

        return ret;
    }
}
