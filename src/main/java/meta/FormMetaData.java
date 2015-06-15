package meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormMetaData {

    private String title;
    private String tableName;
    private String condition;
    private List<String> hideColumns = new ArrayList<>();
    private Map<String, Lookup> lookupMap = new HashMap<>();
    private Map<String, String> defaultValues = new HashMap<>();

    private List<Zoom> zoomData = new ArrayList<>();

    private List<NextMetaData> nextData = new ArrayList<>();
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCondition() {
        return condition;
    }

    public List<NextMetaData> getNextData() {
        return nextData;
    }

    public Map<String, Lookup> getLookupMap() {
        return lookupMap;
    }

    public List<String> getHideColumns() {
        return hideColumns;
    }

    public void putLookup(String columnCode, Lookup lookup) {
        this.lookupMap.put(columnCode, lookup);
    }

    public List<Zoom> getZoomData() {
        return zoomData;
    }

    public void setZoomData(List<Zoom> zoomData) {
        this.zoomData = zoomData;
    }

    public Map<String, String> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(Map<String, String> defaultValues) {
        this.defaultValues = defaultValues;
    }
}
