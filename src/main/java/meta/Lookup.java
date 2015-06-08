package meta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejan on 6/7/2015.
 */
public class Lookup {

    private String table;
    private String from;
    private String to;
    private List<String> columns;

    public Lookup() {
    }

    public Lookup(String table, String from, String to, List<String> columns) {
        this.table = table;
        this.from = from;
        this.to = to;
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
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
