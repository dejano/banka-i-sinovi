package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.StatusBar.FormModeEnum;
import messages.ErrorMessages;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.*;


public class CommitAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private Form form;

    public CommitAction(Form form) {
        putValue(SMALL_ICON,
                new ImageIcon(getClass().getResource("/img/commit.gif")));
        putValue(SHORT_DESCRIPTION, "Commit");
        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FormModeEnum mode = form.getMode();
        try {
            int newRowIndex = -1;

            switch (mode) {
                case ADD:
                    newRowIndex = add();
                    break;
                case EDIT:
                    newRowIndex = update();
                    break;
                case SEARCH:
                    search();
                    break;
            }

            if (newRowIndex != -1)
                form.getDataTable().setRowSelectionInterval(newRowIndex, newRowIndex);
        } catch (SQLException exception) {
            if (exception.getErrorCode() == ErrorMessages.CUSTOM_CODE) {
                JOptionPane.showMessageDialog(form, exception.getMessage(), ErrorMessages.TITLE,
                        JOptionPane.ERROR_MESSAGE);
            } else {
                exception.printStackTrace();
            }
        }
    }

    private int add() throws SQLException {
        java.util.List<String> values = getValues();
        return form.getTableModel().insertRow(values.toArray(new String[values.size()]));
    }

    private int update() throws SQLException {
        java.util.List<String> values = getValues();

        return form.getTableModel().updateRow(form.getDataTable().getSelectedRow(),
                values.toArray(new String[values.size()]));
    }

    private void search() throws SQLException {
        java.util.List<String> values = getValues();
        form.getTableModel().search(values.toArray(new String[values.size()]));
    }

    // TODO move to DataPanel?
    private java.util.List<String> getValues(){
        java.util.List<String> ret = new ArrayList<>();

        for (String columnCode : form.getTableModel().getTableMetaData().getColumnCodes()) {
            boolean setValue = false;

            for (Component component : form.getDataPanel().getComponents()) {
                String componentName = component.getName();
                if (componentName != null && componentName.equals(columnCode)) {
                    if (component instanceof JTextComponent) {
                        ret.add(((JTextComponent) component).getText());
                        // TODO handle other inputs
                        setValue = true;
                    }
                }
            }

            if(!setValue)
                ret.add(form.getTableModel().getTableMetaData().getDefaultValues().get(columnCode));
        }

        return ret;
    }
}
