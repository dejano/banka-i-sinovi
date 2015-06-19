package gui.standard.form.misc;

import rs.mgifos.mosquito.model.MetaColumn;

/**
 * Created by Nikola on 8.6.2015..
 */
public class ColumnData {

    private int index;
    private int baseIndex;
    private String name;
    private String code;
    private boolean primaryKey;
    private boolean foreignKey;
    private String tableName;
    private String className;
    private int length;
    private int precision;
    private boolean mandatory;
    private String nextValue;
    private String defaultValue;
    private boolean lookup;
    private boolean hiddenColumn;
    private boolean hiddenInput;
    private boolean lookupInsert;

    public ColumnData(MetaColumn metaColumn, int index, int baseIndex,
                      boolean lookup, boolean hiddenColumn, boolean hiddenInput, boolean lookupInsert) {
        System.out.println(metaColumn.getCode() + " " + metaColumn.getJClassName()
                + " " + metaColumn.getLength()+ " " + metaColumn.getPrecision());

        this.index = index;
        this.baseIndex = baseIndex;
        this.name = metaColumn.getName();
        this.code = metaColumn.getCode();
        this.tableName = metaColumn.getParentTable();
        this.className = metaColumn.getJClassName();
        this.length = metaColumn.getLength();
        this.precision = metaColumn.getPrecision();
        this.mandatory = metaColumn.isMandatory();
        this.lookup = lookup;
        this.hiddenColumn = hiddenColumn;
        this.hiddenInput = hiddenInput;
        this.lookupInsert = lookupInsert;
        if (!lookup) {
            this.primaryKey = metaColumn.isPartOfPK();
            this.foreignKey = metaColumn.isPartOfFK();
        }
    }

    public ColumnData(String tableName, String code, String className) {
        this.tableName = tableName;
        this.code = code;
        this.className = className;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNextValue() {
        return nextValue;
    }

    public void setNextValue(String nextValue) {
        this.nextValue = nextValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isLookup() {
        return lookup;
    }

    public void setLookup(boolean lookup) {
        this.lookup = lookup;
    }

    public boolean isHiddenColumn() {
        return hiddenColumn;
    }

    public void setHiddenColumn(boolean hiddenColumn) {
        this.hiddenColumn = hiddenColumn;
    }

    public int getBaseIndex() {
        return baseIndex;
    }

    public void setBaseIndex(int baseIndex) {
        this.baseIndex = baseIndex;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isLookupInsert() {
        return lookupInsert;
    }

    public void setLookupInsert(boolean lookupInsert) {
        this.lookupInsert = lookupInsert;
    }

    public boolean isHiddenInput() {
        return hiddenInput;
    }

    public void setHiddenInput(boolean hiddenInput) {
        this.hiddenInput = hiddenInput;
    }
}