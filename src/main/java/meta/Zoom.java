package meta;

import gui.standard.ColumnMapping;

import java.util.List;

public class Zoom {

    private String tableCode;
    private List<ColumnMapping> columns;

    public Zoom(String tableCode, List<ColumnMapping> columns) {
        this.tableCode = tableCode;
        this.columns = columns;
    }

    public Zoom() {
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public List<ColumnMapping> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMapping> columns) {
        this.columns = columns;
    }
}
