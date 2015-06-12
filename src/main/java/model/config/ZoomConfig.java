package model.config;

import java.util.List;

/**
 * Created by dejan on 6/12/2015.
 */
public class ZoomConfig {

    private String table;
    private String columnCode;
    private List<String> columns;


    public ZoomConfig(String table, String columnCode, List<String> columns) {
        this.table = table;
        this.columnCode = columnCode;
        this.columns = columns;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
