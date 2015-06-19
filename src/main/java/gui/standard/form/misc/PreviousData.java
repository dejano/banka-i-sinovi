package gui.standard.form.misc;

import gui.standard.ColumnValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 17.6.2015..
 */
public class PreviousData {

    private List<TableJoin> previousJoins;
    private List<ColumnData> previousColumns;
    private List<String> mapToValues;

    public PreviousData(List<TableJoin> previousJoins, List<ColumnData> previousColumns, List<String> mapToValues) {
        this.previousJoins = previousJoins;
        this.previousColumns = previousColumns;
        this.mapToValues = mapToValues;
    }

    public List<TableJoin> getPreviousJoins() {
        return previousJoins;
    }

    public void setPreviousJoins(List<TableJoin> previousJoins) {
        this.previousJoins = previousJoins;
    }

    public List<ColumnData> getPreviousColumns() {
        return previousColumns;
    }

    public void setPreviousColumns(List<ColumnData> previousColumns) {
        this.previousColumns = previousColumns;
    }

    public List<String> getMapToValues() {
        return mapToValues;
    }

    public void setMapToValues(List<String> mapToValues) {
        this.mapToValues = mapToValues;
    }
}
