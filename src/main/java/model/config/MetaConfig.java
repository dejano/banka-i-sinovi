package model.config;

import java.util.ArrayList;
import java.util.List;

public class MetaConfig {

    private String title;
    private String tableCode;
    private String tableName;
    private List<LookupConfig> lookups = new ArrayList<>();
    private List<ZoomConfig> zoomData = new ArrayList<>();

    public MetaConfig(){}

    public MetaConfig(String title, String tableCode, String tableName, List<LookupConfig> lookups, List<ZoomConfig> zoomConfigs) {
        this.title = title;
        this.tableCode = tableCode;
        this.tableName = tableName;
        this.lookups = lookups;
        this.zoomData = zoomConfigs;
    }

    public List<ZoomConfig> getZoomData() {
        return zoomData;
    }

    public void setZoomData(List<ZoomConfig> zoomData) {
        this.zoomData = zoomData;
    }

    public List<LookupConfig> getLookups() {
        return lookups;
    }

    public void setLookups(List<LookupConfig> lookups) {
        this.lookups = lookups;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "FormMetaData{" +
                "title='" + title + '\'' +
                ", tableCode='" + tableCode + '\'' +
                ", tableName='" + tableName + '\'' +
                ", lookups=" + lookups +
                '}';
    }
}
