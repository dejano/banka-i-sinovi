package actions.standard;

import gui.dialog.Toast;
import gui.standard.form.Form;
import gui.standard.form.StatusBar.FormModeEnum;
import gui.standard.form.components.IValidationTextField;
import messages.ErrorMessages;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.BASE;


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
        if(!validate()){
            System.out.println("INVALID");
        } else {
            FormModeEnum mode = form.getMode();
            try {
                int newRowIndex = -1;

                switch (mode) {
                    case ADD:
                        newRowIndex = add();

                        Toast.show(form, "Slog uspešno dodat.");
                        break;
                    case EDIT:
                        newRowIndex = update();

                        Toast.show(form, "Slog uspešno izmenjen.");
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

    private boolean validate(){
        boolean ret = true;

        for (Component component : form.getDataPanel().getComponents()) {
            if (component instanceof IValidationTextField) {
                IValidationTextField validationTextField = (IValidationTextField) component;
                ret &= validationTextField.isEditValid();
            }
        }

        return ret;
    }

    // TODO move to DataPanel?
    private java.util.List<String> getValues(){
        java.util.List<String> ret = new ArrayList<>();

        for (String columnCode : form.getTableModel().getFormData().getColumnCodes(BASE)) {
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
