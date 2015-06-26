package meta;

import gui.standard.ColumnMapping;

import java.util.ArrayList;
import java.util.List;

public class Zoom {

    private String tableCode;
    private ColumnMapping columnMapping;

    public Zoom(String tableCode, ColumnMapping columnMapping) {
        this.tableCode = tableCode;
        this.columnMapping = columnMapping;
    }

    public boolean isZoomColumn(String columnCode) {
        return columnMapping.getFrom().equals(columnCode);
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public ColumnMapping getColumnMapping() {
        return columnMapping;
    }

    public void setColumnMapping(ColumnMapping columnMapping) {
        this.columnMapping = columnMapping;
    }
}
