package actions;

import gui.standard.form.Form;
import messages.WarningMessages;
import meta.FormCreator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

/**
 * Created by Nikola on 11.6.2015..
 */
public class OpenFormAction extends AbstractAction {

    private String formName;

    public OpenFormAction(String name, String formName) {
        super(name);

        this.formName = formName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Form form = null;

        try {
            form = FormCreator.getStandardForm(formName);
        } catch (SQLException exception) {
            // TODO extract exception handle form messages
            if (exception.getErrorCode() == WarningMessages.CUSTOM_CODE) {
                JOptionPane.showMessageDialog(form, exception.getMessage(), WarningMessages.TITLE,
                        JOptionPane.WARNING_MESSAGE);
            } else {
                exception.printStackTrace();
            }
        }

        form.setVisible(true);
    }
}
