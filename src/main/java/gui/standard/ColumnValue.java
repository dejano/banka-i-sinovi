package gui.standard;

public class ColumnValue {
    private String code;
    private Object value;

    public ColumnValue(String code, Object value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Column{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}