package gui.standard.form;

/**
 * Created by Nikola on 8.6.2015..
 */
public class TableJoin {
    private String tableName;
    private String joinColumn;

    public TableJoin(String tableName, String joinColumn) {
        this.tableName = tableName;
        this.joinColumn = joinColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getJoinColumn() {
        return joinColumn;
    }

    public void setJoinColumn(String joinColumn) {
        this.joinColumn = joinColumn;
    }
}
