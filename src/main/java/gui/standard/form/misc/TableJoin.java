package gui.standard.form.misc;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableJoin {
    private String tableName;
    private String alias;
    private String fromColumn;
    private String toColumn;

    public TableJoin(String tableName, String alias, String fromColumn, String toColumn) {
        this.tableName = tableName;
        this.alias = alias;
        this.fromColumn = fromColumn;
        this.toColumn = toColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(String fromColumn) {
        this.fromColumn = fromColumn;
    }

    public String getToColumn() {
        return toColumn;
    }

    public void setToColumn(String toColumn) {
        this.toColumn = toColumn;
    }
}
