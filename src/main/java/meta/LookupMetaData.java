package meta;

import gui.standard.form.misc.ColumnCodeName;

import java.util.List;

public class LookupMetaData {

    private boolean lookupInsert;
    private String table;
    private String from;
    private String to;
    private List<ColumnCodeName> columns;

    public LookupMetaData() {
    }

    public List<ColumnCodeName> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnCodeName> columns) {
        this.columns = columns;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isLookupInsert() {
        return lookupInsert;
    }

    public void setLookupInsert(boolean lookupInsert) {
        this.lookupInsert = lookupInsert;
    }

    @Override
    public String toString() {
        return "Lookup{" +
                "table='" + table + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", columns=" + columns +
                '}';
    }
}
