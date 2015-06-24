package meta;

import gui.standard.ColumnMapping;

import java.util.ArrayList;
import java.util.List;

public class Zoom {

    private String jsonFileName;
    private String tableCode;
    private List<ColumnMapping> columns;

    public Zoom(String tableCode, List<ColumnMapping> columns) {
        this.jsonFileName = jsonFileName;
        this.tableCode = tableCode;
        this.columns = columns;
    }

    public List<String> getToColumnCodes() {
        List<String> ret = new ArrayList<>();

        for (ColumnMapping column : columns) {
            ret.add(column.getTo());
        }

        return ret;
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

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }
}
