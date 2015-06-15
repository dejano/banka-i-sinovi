package gui.standard.form.misc;

import gui.standard.ColumnMapping;
import meta.Lookup;
import meta.MosquitoSingletone;
import meta.Zoom;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import java.util.*;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableMetaData {

    private String tableName;
    private String condition;
    private Map<String, ColumnMetaData> columns = new LinkedHashMap<>();
    private List<String> primaryKeyColumns = new ArrayList<>();
    private Map<String, TableJoin> lookupJoins = new HashMap<>();
    private Map<String, String> defaultValues = new HashMap<>();


    private List<Zoom> zoomData;

    public TableMetaData(MetaTable metaTable, String condition, Map<String, Lookup> lookups,
                         List<Zoom> zoomData, Map<String, String> defaultValues) {
        this.zoomData = zoomData;
        this.tableName = metaTable.getCode();
        this.condition = condition;
        this.defaultValues = defaultValues;

        Vector<MetaColumn> metaColumns = (Vector<MetaColumn>) metaTable.cColumns();
        int columnIndex = 0;
        for (int i = 0; i < metaTable.getTotalColumns(); i++) {
            MetaColumn column = metaColumns.get(i);

            Lookup lookup = lookups.get(column.getCode());

            if (lookup == null) {
                columns.put(column.getCode(), new ColumnMetaData(column, columnIndex++, false));
            } else {
                columns.put(column.getCode(), new ColumnMetaData(column, columnIndex++, false));
                lookupJoins.put(column.getCode(), new TableJoin(lookup.getTable(), lookup.getTo()));

                MetaTable lookupTable = MosquitoSingletone.getInstance().getMetaTable(lookup.getTable());

                for (Lookup.LookupColumn lookupColumnCode : lookup.getColumns()) {
                    MetaColumn lookupMetaColumn = (MetaColumn) lookupTable
                            .getColByTableDotColumnCode(lookupColumnCode.getCode());
                    ColumnMetaData columnMetaData = new ColumnMetaData(lookupMetaColumn, columnIndex++, true);
                    columnMetaData.setName(lookupColumnCode.getName());
                    columns.put(lookupMetaColumn.getCode(), columnMetaData);
                }

            }

            if (column.isPartOfPK())
                primaryKeyColumns.add(column.getCode());
        }
    }

    public Vector<String> getColumnNames() {
        Vector<String> columnNames = new Vector<>(columns.size());

        for (ColumnMetaData columnMetaData : columns.values()) {
            columnNames.add(columnMetaData.getName());
        }

        return columnNames;
    }

    public Set<String> getColumnCodes() {
        return columns.keySet();
    }

    public Map<String, String> getColumnCodeTypes(ColumnGroupsEnum columnGroup) {
        Map<String, String> ret = new LinkedHashMap<>();

        for (ColumnMetaData columnMetaData : columns.values()) {
            boolean validInsert = true;

            switch (columnGroup) {
                case ALL_WITHOUT_LOOKUP:
                    if (columnMetaData.isLookupColumn())
                        validInsert = false;
                    break;
                case PRIMARY_KEYS:
                    if (!columnMetaData.isPrimaryKey())
                        validInsert = false;
                    break;
            }

            if (validInsert)
                ret.put(columnMetaData.getCode(),
                        columnMetaData.getClassName());
        }

        return ret;
    }

    public int getColumnIndex(String columnCode) {
        return columns.get(columnCode).getIndex();
    }

    public Map<String, ColumnMetaData> getColumns() {
        return columns;
    }

    public Map<String, ColumnMetaData> getBaseColumns() {
        Map<String, ColumnMetaData> result = new LinkedHashMap<>();
        for (Map.Entry<String, ColumnMetaData> entry : columns.entrySet()) {
            if (!entry.getValue().isLookupColumn()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public Map<String, ColumnMetaData> getLookupColumns() {
        Map<String, ColumnMetaData> result = new LinkedHashMap<>();
        for (Map.Entry<String, ColumnMetaData> entry : columns.entrySet()) {
            if (entry.getValue().isLookupColumn()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public Map<String, ColumnMetaData> getLookupColumns(String tableCode) {
        Map<String, ColumnMetaData> result = new LinkedHashMap<>();
        for (Map.Entry<String, ColumnMetaData> entry : columns.entrySet()) {
            if (entry.getValue().isLookupColumn() && entry.getValue().getTableName().equals(tableCode)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    public List<String> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public String getTableName() {
        return tableName;
    }

    public Map<String, TableJoin> getLookupJoins() {
        return lookupJoins;
    }

    public String getCondition() {
        return condition;
    }

    public List<String> getBaseColumnCodes() {
        List<String> result = new ArrayList<>();
        for (ColumnMetaData columnMetaData : columns.values()) {
            if (!columnMetaData.isLookupColumn()) {
                result.add(columnMetaData.getCode());
            }
        }

        return result;
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

    public List<Zoom> getZoomData() {
        return zoomData;
    }

    public void setZoomData(List<Zoom> zoomData) {
        this.zoomData = zoomData;
    }

    public Map<String, String> getDefaultValues() {
        return defaultValues;
    }

    public enum ColumnGroupsEnum {
        ALL, ALL_WITHOUT_LOOKUP, PRIMARY_KEYS;
    }
}

