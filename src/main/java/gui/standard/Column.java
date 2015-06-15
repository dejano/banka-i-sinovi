package gui.standard;

public class Column {
    private String name;
    private Object value;

    public Column() {
    }

    public Column(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}