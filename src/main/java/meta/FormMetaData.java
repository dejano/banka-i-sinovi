package meta;

import app.AppData;
import gui.standard.form.Form;
import gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gui.standard.form.Form.FormType.DEFAULT;
import static gui.standard.form.Form.FormType.READ_ONLY;

public class FormMetaData {

    private String title;
    private Form.FormType formType = DEFAULT;
    private String tableName;
    private String customWhere;
    private String customOrderBy;
    private List<String> additionalActions = new ArrayList<>();
    private List<String> hideColumns = new ArrayList<>();
    private List<String> hideInputs = new ArrayList<>();
    private List<String> nonEditableColumns = new ArrayList<>();
    private Map<ProcedureCallEnum, String> customProcedures = new HashMap<>();
    private Map<String, String> defaultValues = new HashMap<>();
    private Map<String, String> defaultNewValues = new HashMap<>();
    private Map<String, LookupMetaData> lookupMap = new HashMap<>();
    private List<Zoom> zoomData = new ArrayList<>();
    private List<NextMetaData> nextData = new ArrayList<>();
    private Map<AppData.AppDataEnum, String> mapToAppData = new HashMap<>();

    public boolean isZoomColumn(String columnCode){
        boolean ret = false;

        for (Zoom zoom : zoomData) {
            if(zoom.isZoomColumn(columnCode)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Form.FormType getFormType() {
        return formType;
    }

    public void setFormType(Form.FormType formType) {
        this.formType = formType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCustomWhere() {
        return customWhere;
    }

    public void setCustomWhere(String customWhere) {
        this.customWhere = customWhere;
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

    public Map<AppData.AppDataEnum, String> getMapToAppData() {
        return mapToAppData;
    }

    public void setMapToAppData(Map<AppData.AppDataEnum, String> mapToAppData) {
        this.mapToAppData = mapToAppData;
    }

    public List<String> getHideInputs() {
        return hideInputs;
    }

    public void setHideInputs(List<String> hideInputs) {
        this.hideInputs = hideInputs;
    }

    public Map<ProcedureCallEnum, String> getCustomProcedures() {
        return customProcedures;
    }

    public void setCustomProcedures(Map<ProcedureCallEnum, String> customProcedures) {
        this.customProcedures = customProcedures;
    }

    public String getCustomOrderBy() {
        return customOrderBy;
    }

    public void setCustomOrderBy(String customOrderBy) {
        this.customOrderBy = customOrderBy;
    }

    public boolean isReadOnly() {
        return formType == READ_ONLY;
    }

    public List<String> getAdditionalActions() {
        return additionalActions;
    }

    public void setAdditionalActions(List<String> additionalActions) {
        this.additionalActions = additionalActions;
    }

    public Map<String, String> getDefaultNewValues() {
        return defaultNewValues;
    }

    public List<String> getNonEditableColumns() {
        return nonEditableColumns;
    }

    public void setNonEditableColumns(List<String> nonEditableColumns) {
        this.nonEditableColumns = nonEditableColumns;
    }

    public void setDefaultNewValues(Map<String, String> defaultNewValues) {
        this.defaultNewValues = defaultNewValues;
    }
}
