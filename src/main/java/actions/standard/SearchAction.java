package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.StatusBar;
import messages.WarningMessages;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.*;

public class SearchAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private Form form;

    public SearchAction(Form form) {
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/search.gif")));
        putValue(SHORT_DESCRIPTION, "Pretraga");
        this.form = form;
    }

    public void actionPerformed(ActionEvent e) {
        form.setMode(StatusBar.FormModeEnum.SEARCH);
        try {
            // TODO use real values
            form.getTableModel().search(new String[]{"", "BHA", "", "", "", "", "", ""});
        } catch (SQLException exception) {
            if (exception.getErrorCode() == WarningMessages.CUSTOM_CODE) {
                JOptionPane.showMessageDialog(form, exception.getMessage(), WarningMessages.TITLE,
                        JOptionPane.WARNING_MESSAGE);
            } else {
                exception.printStackTrace();
            }
        }
    }
}
