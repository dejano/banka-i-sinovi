package actions;

import app.App;
import gui.dialog.ErrorMessageDialog;
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
            ErrorMessageDialog.show(App.getMainFrame(), exception);
        }

        form.setVisible(true);
    }
}
