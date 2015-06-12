package model.db;

import model.Model;
import model.config.ZoomConfig;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by dejan on 6/8/2015.
 */
public class SchemaRow extends Model {

    private String tableCode;
    private String tableName;
    private List<SchemaColumn> schemaColumns;
    private List<SchemaColumn> primaryKeys;
    private Map<SchemaColumn, List<SchemaColumn>> foreignKeys;
    private List<String> lookupTableCodes;
    private List<ZoomConfig> zoomList;

    public SchemaRow(String tableCode, String tableName, List<SchemaColumn> schemaColumns, List<SchemaColumn> primaryKeys, Map<SchemaColumn, List<SchemaColumn>> foreignKeys,
                     List<String> lookupTableCodes, List<ZoomConfig> zoomList) {
        this.tableCode = tableCode;
        this.tableName = tableName;
        this.schemaColumns = schemaColumns;
        this.primaryKeys = primaryKeys;
        this.foreignKeys = foreignKeys;
        this.lookupTableCodes = lookupTableCodes;
        this.zoomList = zoomList;
    }

    public List<ZoomConfig> getZoomList() {
        return zoomList;
    }

    public String getTableCode() {
        return tableCode;
    }

    public String getTableName() {
        return tableName;
    }

    public List<SchemaColumn> getSchemaColumns() {
        return schemaColumns;
    }

    public List<SchemaColumn> getPrimaryKeys() {
        return primaryKeys;
    }

    public Map<SchemaColumn, List<SchemaColumn>> getForeignKeys() {
        return foreignKeys;
    }

    public List<String> getLookupTableCodes() {
        return lookupTableCodes;
    }

    public void setLookupTableCodes(List<String> lookupTableCodes) {
        this.lookupTableCodes = lookupTableCodes;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableCode='" + tableCode + '\'' +
                ", tableName='" + tableName + '\'' +
                ", columns=" + schemaColumns +
                ", primaryKeys=" + primaryKeys +
                ", foreignKeys=" + foreignKeys +
                '}';
    }

    public Vector<String> getColumnNames() {
        Vector<String> columnNames = new Vector<String>();
        for (SchemaColumn schemaColumn : getSchemaColumns()) {
            columnNames.add(schemaColumn.getName());
        }
        return columnNames;
    }

    public Vector<String> getColumnCodes() {
        Vector<String> columnCodes = new Vector<String>();
        for (SchemaColumn schemaColumn : getSchemaColumns()) {
            columnCodes.add(schemaColumn.getCode());
        }
        return columnCodes;
    }

    public Vector<String> getColumnCodesFromBaseTableOnly() {
        Vector<String> columnCodes = new Vector<String>();
        for (SchemaColumn schemaColumn : getSchemaColumns()) {
            if (schemaColumn.getTableName().equals(tableCode)) {
                columnCodes.add(schemaColumn.getCode());
            }
        }
        return columnCodes;
    }

    public Vector<SchemaColumn> getColumnsFromBaseTableOnly() {
        Vector<SchemaColumn> columnCodes = new Vector<SchemaColumn>();
        for (SchemaColumn schemaColumn : getSchemaColumns()) {
            if (schemaColumn.getTableName().equals(tableCode)) {
                columnCodes.add(schemaColumn);
            }
        }
        return columnCodes;
    }

    public Vector<String> getForeignColumnCodes() {
        Vector<String> columnCodes = new Vector<String>();
        for (SchemaColumn schemaColumn : getSchemaColumns()) {
            if (!schemaColumn.getTableName().equals(tableCode)) {
                columnCodes.add(schemaColumn.getCode());
            }
        }
        return columnCodes;
    }

    public Vector<String> getForeignColumnCodes(String tableCode) {
        Vector<String> columnCodes = new Vector<String>();
        for (SchemaColumn schemaColumn : getSchemaColumns()) {
            if (schemaColumn.getTableName().equals(tableCode)) {
                columnCodes.add(schemaColumn.getCode());
            }
        }
        return columnCodes;
    }

    public Vector<String> getForeignColumnNames(String tableCode) {
        Vector<String> columnNames = new Vector<String>();
        for (SchemaColumn schemaColumn : getSchemaColumns()) {
            System.out.println(schemaColumn.getCode());
            if (schemaColumn.getTableName().equals(tableCode)) {
                columnNames.add(schemaColumn.getName());
            }
        }
        return columnNames;
    }

    public Vector<String> getLookupColumnCodes(String baseTableCode, String lookupTableCode) {
        Vector<String> columnNames = new Vector<String>();
        for (Map.Entry<SchemaColumn, List<SchemaColumn>> entry : foreignKeys.entrySet()) {
            if (entry.getKey().getTableName().equals(baseTableCode) && lookupTableCode.equals(entry.getValue().get(0).getTableName())) {
                for (SchemaColumn column : entry.getValue()) {
                    columnNames.add(column.getCode());
                }
                columnNames.add(entry.getKey().getCode());
            }
        }

        return columnNames;
    }

}
