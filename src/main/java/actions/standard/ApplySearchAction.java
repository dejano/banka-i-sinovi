package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.StatusBar.FormModeEnum;
import messages.ErrorMessages;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.ALL;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.BASE;


public class ApplySearchAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private Form form;

    public ApplySearchAction(Form form) {
        putValue(SMALL_ICON,
                new ImageIcon(getClass().getResource("/img/search.gif")));
        putValue(SHORT_DESCRIPTION, "Apply search");
        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FormModeEnum mode = form.getMode();
        try {
            search();
        } catch (SQLException exception) {
            if (exception.getErrorCode() == ErrorMessages.CUSTOM_CODE) {
                JOptionPane.showMessageDialog(form, exception.getMessage(), ErrorMessages.TITLE,
                        JOptionPane.ERROR_MESSAGE);
            } else {
                exception.printStackTrace();
            }
        }
    }

    private void search() throws SQLException {
        java.util.List<String> values = getValues();
        form.getTableModel().search(values.toArray(new String[values.size()]));
    }

    // TODO move to DataPanel?
    private java.util.List<String> getValues() {
        java.util.List<String> ret = new ArrayList<>();

        for (String columnCode : form.getTableModel().getFormData().getColumnCodes(ALL)) {
            boolean setValue = false;

            for (Component component : form.getDataPanel().getComponents()) {
                String componentName = component.getName();
                if (componentName != null && componentName.equals(columnCode)) {
                    // TODO handle other inputs
                    if (component instanceof JTextComponent) {
                        ret.add(((JTextComponent) component).getText());
                        setValue = true;
                        break;
                    }
                }
            }

            if(!setValue) {
                String defaultValue =form.getFormData().getDefaultValue(columnCode);
                if(defaultValue != null)
                    ret.add(defaultValue);
                else
                    ret.add(form.getFormData().getNextValue(columnCode));
            }
        }

        return ret;
    }
}
