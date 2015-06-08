package gui.standard.form.misc;

import gui.standard.form.TableJoin;
import meta.Lookup;
import meta.MosquitoSingletone;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import java.util.*;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableMetaData {

    private String tableName;
    private Map<String, ColumnMetaData> columns = new LinkedHashMap<>();
    private List<String> primaryKeyColumns = new ArrayList<>();
    private Map<String, TableJoin> lookupJoins = new HashMap<>();

    public TableMetaData(MetaTable metaTable, Map<String, Lookup> lookups) {
        this.tableName = metaTable.getCode();

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

                for (String lookupColumnCode : lookup.getColumns()) {
                    MetaColumn lookupMetaColumn = (MetaColumn) lookupTable
                            .getColByTableDotColumnCode(lookupTable.getCode() + "." + lookupColumnCode);

                    columns.put(lookupMetaColumn.getCode(), new ColumnMetaData(lookupMetaColumn, columnIndex++, true));
                }

            }

            if (column.isPartOfPK())
                primaryKeyColumns.add(column.getCode());

            // TODO filter out columns - hide, etc
        }
    }

    public Vector<String> getColumnNames() {
        Vector<String> columnNames = new Vector<>(columns.size());

        for (ColumnMetaData columnMetaData : columns.values()) {
            columnNames.add(columnMetaData.getName());
        }

        return columnNames;
    }

    public Map<String, String> getColumnCodeTypes(ColumnGroupsEnum columnGroup) {
        Map<String, String> ret = new LinkedHashMap<>();

        for (ColumnMetaData columnMetaData : columns.values()) {
            boolean validInsert = true;

            switch (columnGroup) {
                case ALL_WITHOUT_LOOKUP:
                    // TODO
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

    public List<String> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public String getTableName() {
        return tableName;
    }

    public Map<String, TableJoin> getLookupJoins() {
        return lookupJoins;
    }

    public enum ColumnGroupsEnum {
        ALL, ALL_WITHOUT_LOOKUP, PRIMARY_KEYS
    }
}

