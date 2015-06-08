package gui.standard;

public class Column {
    private String name;
    private String value;

    public Column() {
    }

    public Column(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}