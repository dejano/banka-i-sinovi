package gui.standard;

import com.sun.xml.internal.txw2.annotation.XmlElement;

/**
 * Created by Nikola on 7.6.2015..
 */
public class ColumnMapping {

    private String columnCode1;
    private String columnCode2;

    public ColumnMapping() {
    }

    public ColumnMapping(String columnCode1, String columnCode2) {
        this.columnCode1 = columnCode1;
        this.columnCode2 = columnCode2;
    }

    public String getColumnCode1() {
        return columnCode1;
    }

    public void setColumnCode1(String columnCode1) {
        this.columnCode1 = columnCode1;
    }

    public String getColumnCode2() {
        return columnCode2;
    }

    public void setColumnCode2(String columnCode2) {
        this.columnCode2 = columnCode2;
    }
}
