package gui.standard;

/**
 * Created by Nikola on 7.6.2015..
 */
public class ColumnMapping {

    private String from;
    private String to;

    public ColumnMapping() {
    }

    public ColumnMapping(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
