package actions.standard;

import gui.dialog.ErrorMessageDialog;
import gui.standard.ColumnMapping;
import gui.standard.form.Form;
import gui.standard.form.misc.ColumnData;
import gui.standard.form.misc.TableJoin;
import meta.FormCreator;
import meta.Zoom;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.LOOKUP;

public class ZoomFormAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private Form standardForm;
    private Zoom zoom;

    public ZoomFormAction(Form standardForm, Zoom zoom) {
        this.zoom = zoom;
        putValue(SHORT_DESCRIPTION, "Zoom");
        putValue(NAME, "...");
        this.standardForm = standardForm;
    }

    public void actionPerformed(ActionEvent event) {
        try {
            Form zoomForm = FormCreator.getStandardForm(zoom.getTableCode());

            Map<String, String> zoomData = new HashMap<>();
            zoomData.put(zoom.getColumnMapping().getTo(), null);

            Map<String, ColumnData> lookupColumns = standardForm.getFormData().getColumnsMap(LOOKUP);
            for (Map.Entry<String, ColumnData> entry : lookupColumns.entrySet()) {
                if (entry.getValue().getRealTableName().equals(zoomForm.getFormData().getTableName())) {
                    zoomData.put(entry.getKey(), null);
                }
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

            String removeFrom = null;
            String removeFromTable = null;
            String untouchedColumn = null;

            if (!Objects.equals(zoom.getTableCode(), event.getActionCommand())) {
                for (String key : results.keySet()) {
                    if (zoom.getColumnMapping().getTo().equals(key)) {
                        removeFrom = zoom.getTableCode();
                        untouchedColumn = key;
                        removeFromTable = zoom.getTableCode();
                    }
                }
            }

            for (TableJoin lookup : standardForm.getTableModel().getFormData().getLookupJoins(removeFromTable)) {
                for (Component component : standardForm.getDataPanel().getComponents()) {
                    if (component instanceof JTextComponent && component.getName().equals(lookup.getFromColumn())) {
                        JTextComponent textComponent = (JTextComponent) component;
                        textComponent.setText("");
                    }
                }
            }

            String zoomResult = results.get(zoom.getColumnMapping().getTo());
            results.remove(zoom.getColumnMapping().getTo());

            standardForm.getDataPanel().setValue(zoom.getColumnMapping().getFrom(), zoomResult, false);

            for (Map.Entry<String, String> entry : results.entrySet()) {
                standardForm.getDataPanel().setValue(entry.getKey(), entry.getValue(), false);
            }

            if (removeFrom != null) {
                System.out.println("DELETE FROM:");
                List<String> toRemove = new ArrayList<>();
                for (String columnName : standardForm.getTableModel().getFormData().getZoomColumns(removeFrom)) {
                    if (!Objects.equals(columnName, untouchedColumn)) {
                        toRemove.add(columnName);
                        System.out.println(columnName);
                    }
                }


                for (String removeName : toRemove) {
                    for (Component component : standardForm.getDataPanel().getComponents()) {
                        if (component instanceof JTextComponent && component.getName().equals(removeName)) {
                            JTextComponent textComponent = (JTextComponent) component;
                            System.out.println(toRemove);
                            textComponent.setText("");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            ErrorMessageDialog.show(standardForm, e);
        }
    }
}
