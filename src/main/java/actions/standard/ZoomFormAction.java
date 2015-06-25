package actions.standard;

import gui.standard.ColumnMapping;
import gui.dialog.ErrorMessageDialog;
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

    public ZoomFormAction(Form standardForm, String actionCommand) {
        putValue(ACTION_COMMAND_KEY, actionCommand);
        putValue(SHORT_DESCRIPTION, "Zoom");
        putValue(NAME, "...");
        this.standardForm = standardForm;
    }

    public void actionPerformed(ActionEvent event) {
        try {
//            Form zoomForm = FormCreator.getStandardForm(event.getActionCommand());

            List<Zoom> zoomConfig = standardForm.getTableModel().getFormData().getZoomData();

            List<String> zoomColumns = standardForm.getTableModel().getFormData().getZoomColumns(event.getActionCommand());


            String mutualCol = null;
            if (zoomColumns.size() > 1) {
                for (Zoom zoom : zoomConfig) {
                    if (!zoom.getTableCode().equals(event.getActionCommand())) {
                        for (ColumnMapping columnMapping : zoom.getColumns()) {
                            if (zoomColumns.contains(columnMapping.getFrom())) {
                                System.out.println("Mutual:" + columnMapping.getFrom());
                                mutualCol = columnMapping.getFrom();
                            }
                        }
                    }
                }
            }

            String filterVal = null;
            if (mutualCol != null) {
                for (Component component : standardForm.getDataPanel().getComponents()) {
                    if (component instanceof JTextComponent && component.getName().equals(mutualCol)) {
                        JTextComponent textComponent = (JTextComponent) component;
                        filterVal = textComponent.getText();
                    }
                }
            }

            Form zoomForm = null;
            if (mutualCol != null && filterVal != null && !filterVal.isEmpty()) {
                Map<String, String> nextColumns = new HashMap<>();
                nextColumns.put(mutualCol, filterVal);
                System.out.println("Next -> " + mutualCol + ":" + filterVal);
                zoomForm = FormCreator.getNextStandardForm(event.getActionCommand(), nextColumns);
            } else {
                zoomForm = FormCreator.getStandardForm(event.getActionCommand());
            }

            Map<String, String> zoomData = new HashMap<>();
            for (String zoomColumn : zoomColumns) {
                zoomData.put(zoomColumn, null);
            }

            Map<String, ColumnData> lookupColumns = standardForm.getFormData().getColumnsMap(LOOKUP);
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

            String removeFrom = null;
            String removeFromTable = null;
            String untouchedColumn = null;

            for (Zoom zoom : zoomConfig) {
                if (!Objects.equals(zoom.getTableCode(), event.getActionCommand())) {
                    for (ColumnMapping columnMapping : zoom.getColumns()) {
                        for (String key : results.keySet()) {
                            if (columnMapping.getTo().equals(key)) {
                                removeFrom = zoom.getTableCode();
                                untouchedColumn = key;
                                removeFromTable = zoom.getTableCode();
                                System.out.println("Contains " + columnMapping.getTo() + " in " + zoom.getTableCode());
                            }
                        }
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

            System.out.println();

            for (Map.Entry<String, String> entry : results.entrySet()) {
                for (Component component : standardForm.getDataPanel().getComponents()) {
                    if (component instanceof JTextComponent && component.getName().equals(entry.getKey())) {
                        JTextComponent textComponent = (JTextComponent) component;
                        textComponent.setText(entry.getValue());
                    }
                }
                System.out.println(entry.getKey() + ":" + entry.getValue());
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

//            standardForm.onZoomDialogClosed(results);
        } catch (SQLException e) {
            ErrorMessageDialog.show(standardForm, e);
        }
    }
}
