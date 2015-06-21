package gui.standard.form.misc;

import gui.standard.ColumnMapping;
import gui.standard.ColumnValue;
import gui.standard.form.Form;
import gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum;
import meta.*;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import java.util.*;

import static gui.standard.form.Form.FormType.PANEL;
import static gui.standard.form.Form.FormType.READ_ONLY;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.BASE;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.NEXT;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.PRIMARY_KEYS;

/**
 * Created by Nikola on 8.6.2015..
 */
public class FormData {

    private String tableName;
    private Form.FormType formType;
    private String condition;
    private String customOrderBy;
    private Map<ProcedureCallEnum, String> customProcedures = new HashMap<>();
    private List<TableJoin> lookupJoins = new ArrayList<>();
    private List<NextMetaData> nextForms;
    private Map<String, ColumnData> columns = new LinkedHashMap<>();
    private List<Zoom> zoomData;

    public FormData(MetaTable metaTable, FormMetaData fmd, Map<String, String> nextColumnCodeValues) {
        this.formType = fmd.getFormType();
        this.zoomData = fmd.getZoomData();
        this.tableName = metaTable.getCode();
        this.condition = fmd.getCondition();
        this.condition = fmd.getCondition();
        this.customOrderBy = fmd.getCustomOrderBy();
        this.customProcedures = fmd.getCustomProcedures();

        this.nextForms = fmd.getNextData();

        Vector<MetaColumn> metaColumns = (Vector<MetaColumn>) metaTable.cColumns();
        int columnIndex = 0;
        int baseIndex = 0;
        for (int i = 0; i < metaTable.getTotalColumns(); i++) {
            MetaColumn column = metaColumns.get(i);
            String defaultValue = fmd.getDefaultValues().get(column.getCode());
            LookupMetaData lookupMetaData = fmd.getLookupMap().get(column.getCode());
            boolean hiddenColumn = fmd.getHideColumns().contains(column.getCode());
            boolean hiddenInput = fmd.getHideInputs().contains(column.getCode());
            String nextValue = null;
            if (nextColumnCodeValues != null)
                nextValue = nextColumnCodeValues.get(column.getCode());

            ColumnData newColumnData = new ColumnData(column, columnIndex++, baseIndex++, false,
                    hiddenColumn, hiddenInput, false);

            newColumnData.setDefaultValue(defaultValue);
            newColumnData.setNextValue(nextValue);

            if (lookupMetaData != null)
                lookupJoins.add(new TableJoin(lookupMetaData.getTable(), lookupMetaData.getFrom(),
                        lookupMetaData.getTo()));

            columns.put(column.getCode(), newColumnData);

            if (lookupMetaData != null) {
                MetaTable lookupTable = MosquitoSingletone.getInstance().getMetaTable(lookupMetaData.getTable());

                for (ColumnCodeName columnCodeValueCode : lookupMetaData.getColumns()) {
                    MetaColumn lookupMetaColumn = (MetaColumn) lookupTable
                            .getColByTableDotColumnCode(lookupTable.getCode() + "." + columnCodeValueCode.getCode());

                    ColumnData newLookupColumnData = new ColumnData(lookupMetaColumn, columnIndex++,
                            lookupMetaData.isLookupInsert()? baseIndex++:-1, true,
                            false, false, lookupMetaData.isLookupInsert());
                    newLookupColumnData.setName(columnCodeValueCode.getName());

                    columns.put(lookupMetaColumn.getCode(), newLookupColumnData);
                }
            }
        }
    }

    public boolean isInGroup(String columnCode, ColumnGroupsEnum... columnGroups) {
        boolean ret = false;

        ColumnData columnData = columns.get(columnCode);
        if (columnData != null) {
            for (ColumnGroupsEnum columnGroup : columnGroups) {
                ret |= isInGroup(columnData, columnGroup);
            }
        }
        return ret;
    }

    public List<ColumnValue> mapValues(ColumnGroupsEnum columnGroup, String[] values) {
        List<ColumnValue> ret = new ArrayList<>();

        List<String> columnCodes = getColumnCodes(columnGroup);

        for (int i = 0; i < columnCodes.size(); i++) {
            ret.add(new ColumnValue(columnCodes.get(i), values[i]));
        }

        return ret;
    }

    public Vector<String> getColumnNames() {
        Vector<String> columnNames = new Vector<>(columns.size());

        for (ColumnData columnData : columns.values()) {
            columnNames.add(columnData.getName());
        }

        return columnNames;
    }

    public Map<String, String> getColumnCodeTypes(ColumnGroupsEnum columnGroup) {
        Map<String, String> ret = new LinkedHashMap<>();

        for (ColumnData columnData : columns.values()) {
            if (isInGroup(columnData, columnGroup))
                ret.put(columnData.getCode(), columnData.getClassName());
        }

        return ret;
    }

    public boolean isEditable(String columnCode) {
        return isInGroup(columnCode, BASE) && !isInGroup(columnCode, NEXT, PRIMARY_KEYS);
    }

    public int getColumnIndex(String columnCode, boolean useBase) {
        ColumnData columnData = columns.get(columnCode);
        return columnData == null ? null : (useBase ? columnData.getBaseIndex() : columnData.getIndex());
    }

    public Map<String, ColumnData> getColumns(ColumnGroupsEnum columnGroup) {
        Map<String, ColumnData> ret = new LinkedHashMap<>();

        for (ColumnData columnData : columns.values()) {
            if (isInGroup(columnData, columnGroup))
                ret.put(columnData.getCode(), columnData);
        }

        return columns;
    }

    public List<String> getColumnCodes(ColumnGroupsEnum columnGroup) {
        List<String> ret = new ArrayList<>();

        for (ColumnData columnData : columns.values()) {
            if (isInGroup(columnData, columnGroup))
                ret.add(columnData.getCode());
        }

        return ret;
    }

    public List<String> getTableDotCodes(ColumnGroupsEnum columnGroup) {
        List<String> ret = new ArrayList<>();

        for (ColumnData columnData : columns.values()) {
            if (isInGroup(columnData, columnGroup))
                ret.add(columnData.getTableName() + "." + columnData.getCode());
        }

        return ret;
    }

    public List<Integer> getColumnIndexes(ColumnGroupsEnum columnGroup, boolean useBase) {
        List<Integer> ret = new ArrayList<>();

        for (ColumnData columnData : columns.values()) {
            if (isInGroup(columnData, columnGroup)) {
                if (useBase) {
                    ret.add(columnData.getBaseIndex());
                } else {
                    ret.add(columnData.getIndex());
                }
            }
        }

        return ret;
    }

    public List<ColumnValue> getNextValues() {
        List<ColumnValue> ret = new ArrayList<>();

        for (ColumnData columnData : columns.values()) {
            if (isInGroup(columnData, NEXT))
                ret.add(new ColumnValue(columnData.getCode(), columnData.getNextValue()));
        }

        return ret;
    }

    public Map<String, String> getNextValuesMap() {
        Map<String, String> ret = new LinkedHashMap<>();

        for (ColumnData columnData : columns.values()) {
            if (isInGroup(columnData, NEXT))
                ret.put(columnData.getCode(), columnData.getNextValue());
        }

        return ret;
    }

    private boolean isInGroup(ColumnData columnData, ColumnGroupsEnum columnGroup) {
        boolean ret = true;

        switch (columnGroup) {
            case PRIMARY_KEYS:
                ret = columnData.isPrimaryKey();
                break;
            case NEXT:
                ret = columnData.getNextValue() != null;
                break;
            case LOOKUP:
                ret = columnData.isLookup();
                break;
            case BASE:
                ret = !columnData.isLookup() || columnData.isLookupInsert();
                break;
            case LOOKUP_INSERT:
                ret = columnData.isLookupInsert();
                break;
        }

        return ret;
    }

    public String[] extractPkValues(String[] values, boolean useBase) {
        String[] ret;

        List<Integer> pkIndexes = getColumnIndexes(PRIMARY_KEYS, useBase);

        ret = new String[pkIndexes.size()];

        for (int i = 0; i < pkIndexes.size(); i++) {
            ret[i] = values[pkIndexes.get(i)];
        }

        return ret;
    }

    public int getCount(ColumnGroupsEnum columnGroup) {
        int ret = 0;

        for (ColumnData columnData : columns.values()) {
            if (isInGroup(columnData, columnGroup))
                ret++;
        }

        return ret;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCondition() {
        return condition;
    }

    public List<String> getZoomBaseColumns() {
        List<String> result = new ArrayList<>();
        for (Zoom zoom : zoomData) {
            for (ColumnMapping columnMapping : zoom.getColumns()) {
                result.add(columnMapping.getFrom());
            }
        }

        return result;
    }

    public List<String> getZoomColumns(String tableCode) {
        List<String> result = new ArrayList<>();
        for (Zoom zoom : zoomData) {
            if (tableCode.equals(zoom.getTableCode())) {
                for (ColumnMapping columnMapping : zoom.getColumns()) {
                    result.add(columnMapping.getTo());
                }
            }
        }

        return result;
    }

    public String getZoomTableCode(String columnName) {
        for (Zoom zoom : zoomData) {
            for (ColumnMapping columnMapping : zoom.getColumns()) {
                if (columnMapping.getFrom().equals(columnName)) {
                    return zoom.getTableCode();
                }
            }
        }

        return null;
    }

    public Form.FormType getFormType() {
        return formType;
    }

    public void setFormType(Form.FormType formType) {
        this.formType = formType;
    }

    public List<Zoom> getZoomData() {
        return zoomData;
    }

    public void setZoomData(List<Zoom> zoomData) {
        this.zoomData = zoomData;
    }

    public String getDefaultValue(String columnCode) {
        return columns.get(columnCode).getDefaultValue();
    }

    public String getNextValue(String columnCode) {
        return columns.get(columnCode).getNextValue();
    }

    public List<TableJoin> getLookupJoins() {
        return lookupJoins;
    }

    public void setLookupJoins(List<TableJoin> lookupJoins) {
        this.lookupJoins = lookupJoins;
    }

    public List<NextMetaData> getNextForms() {
        return nextForms;
    }

    public void setNextForms(List<NextMetaData> nextForms) {
        this.nextForms = nextForms;
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

    public boolean isPanelForm() {
        return formType == PANEL;
    }

    public enum ColumnGroupsEnum {
        ALL, BASE, PRIMARY_KEYS, LOOKUP, NEXT, LOOKUP_INSERT
    }
}

