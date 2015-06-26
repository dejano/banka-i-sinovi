package meta;

import gui.standard.ColumnMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 7.6.2015..
 */
public class NextMetaData {

    private String alias;
    private String formName;
    private List<ColumnMapping> columnCodeMapping = new ArrayList<>();

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public List<ColumnMapping> getColumnCodeMapping() {
        return columnCodeMapping;
    }

    public void setColumnCodeMapping(List<ColumnMapping> columnCodeMapping) {
        this.columnCodeMapping = columnCodeMapping;
    }
}