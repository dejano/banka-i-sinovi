package meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormMetaData {

    private String title;
    private boolean readOnly;
    private String tableName;
    private String condition;
    private String customOrderBy;
    private List<String> hideColumns = new ArrayList<>();
    private List<String> hideInputs = new ArrayList<>();
    private Map<String, String> customProcedures = new HashMap<>();
    private Map<String, String> defaultValues = new HashMap<>();
    private Map<String, LookupMetaData> lookupMap = new HashMap<>();
    private List<Zoom> zoomData = new ArrayList<>();
    private List<NextMetaData> nextData = new ArrayList<>();
    private Map<String, String> mapToAppData = new HashMap<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
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

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<String> getHideColumns() {
        return hideColumns;
    }

    public void setHideColumns(List<String> hideColumns) {
        this.hideColumns = hideColumns;
    }

    public Map<String, LookupMetaData> getLookupMap() {
        return lookupMap;
    }

    public void setLookupMap(Map<String, LookupMetaData> lookupMap) {
        this.lookupMap = lookupMap;
    }

    public Map<String, String> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(Map<String, String> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public List<Zoom> getZoomData() {
        return zoomData;
    }

    public void setZoomData(List<Zoom> zoomData) {
        this.zoomData = zoomData;
    }

    public List<NextMetaData> getNextData() {
        return nextData;
    }

    public void setNextData(List<NextMetaData> nextData) {
        this.nextData = nextData;
    }

    public Map<String, String> getMapToAppData() {
        return mapToAppData;
    }

    public void setMapToAppData(Map<String, String> mapToAppData) {
        this.mapToAppData = mapToAppData;
    }

    public List<String> getHideInputs() {
        return hideInputs;
    }

    public void setHideInputs(List<String> hideInputs) {
        this.hideInputs = hideInputs;
    }

    public Map<String, String> getCustomProcedures() {
        return customProcedures;
    }

    public void setCustomProcedures(Map<String, String> customProcedures) {
        this.customProcedures = customProcedures;
    }

    public String getCustomOrderBy() {
        return customOrderBy;
    }

    public void setCustomOrderBy(String customOrderBy) {
        this.customOrderBy = customOrderBy;
    }
}
