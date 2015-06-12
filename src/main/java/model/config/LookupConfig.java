package model.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejan on 6/7/2015.
 */
public class LookupConfig {

    private String table;
    private String from;
    private String to;
    private List<LookupColumn> columns;

    public LookupConfig(String table, String from, String to, List<LookupColumn> columns) {
        this.table = table;
        this.from = from;
        this.to = to;
        this.columns = columns;
    }

    public List<LookupColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<LookupColumn> columns) {
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

    public class LookupColumn {
        private String name;
        private String label;

        public LookupColumn() {
        }

        public LookupColumn(String name, String label) {
            this.name = name;
            this.label = label;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return "LookupColumn{" +
                    "name='" + name + '\'' +
                    ", label='" + label + '\'' +
                    '}';
        }
    }
}
