package gui.standard.form.misc;

import rs.mgifos.mosquito.model.MetaColumn;

/**
 * Created by Nikola on 8.6.2015..
 */
public class ColumnMetaData {

    private int index;
    private String name;
    private String code;
    private boolean primaryKey;
    private boolean foreignKey;
    private boolean lookupColumn;
    private String tableName;
    private String className;

    public ColumnMetaData(MetaColumn metaColumn, int index, boolean isLookup) {
        this.index = index;
        this.name = metaColumn.getName();
        this.code = metaColumn.getCode();
        this.tableName = metaColumn.getParentTable();
        this.className = metaColumn.getJClassName();
        this.lookupColumn = isLookup;

        if (!isLookup) {
            this.primaryKey = metaColumn.isPartOfPK();
            this.foreignKey = metaColumn.isPartOfFK();
        }

    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public String getTableName() {
        return tableName;
    }

    public String getClassName() {
        return className;
    }

    public boolean isLookupColumn() {
        return lookupColumn;
    }
}
