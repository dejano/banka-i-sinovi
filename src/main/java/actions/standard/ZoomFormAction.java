package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.misc.ColumnMetaData;
import meta.FormCreator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoomFormAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private Form standardForm;

    public ZoomFormAction(Form standardForm, String actionCommand) {
        putValue(ACTION_COMMAND_KEY, actionCommand);
        putValue(SHORT_DESCRIPTION, "Zoom");
        putValue(NAME, "...");
        this.standardForm = standardForm;
    }

    public void actionPerformed(ActionEvent event) {
        try {
            Form zoomForm = FormCreator.getStandardForm(event.getActionCommand());

            List<String> zoomColumns = standardForm.getTableModel().getTableMetaData().getZoomColumns(event.getActionCommand());
            Map<String, String> zoomData = new HashMap<>();
            for (String zoomColumn : zoomColumns) {
                zoomData.put(zoomColumn, null);
            }

            Map<String, ColumnMetaData> lookupColumns = standardForm.getTableModel().getTableMetaData().getLookupColumns(event.getActionCommand());
            for (String s : lookupColumns.keySet()) {
                zoomData.put(s, null);
            }

            zoomForm.setParentForm(standardForm);
            zoomForm.setVisible(true);

            // after pickup button
            int index = zoomForm.getDataTable().getSelectedRow();
            Map<String, String> results = new HashMap<>();

            if (index >= 0) {
                for (String columnCode : zoomData.keySet()) {
                    String columnValue = zoomForm.getSelectedRowValue(columnCode);
                    results.put(columnCode, columnValue);
                }
            }

            standardForm.onZoomDialogClosed(results);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
