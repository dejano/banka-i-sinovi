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
//                    newRowIndex = form.getTableModel().insertRow(
//                            new String[]{"6", "a", "a", "a", "a", "a", "8", "1"} // videoteka
////                            new String[]{"5", "8", "11", "0"} // kopija
//                    );

                    newRowIndex = add();
                    break;
                case EDIT:
                    //				form.getTableModel().updateRow(form.getDataPanel().getValues());
//                    newRowIndex = form.getTableModel().updateRow(form.getDataTable().getSelectedRow(),
//                            new String[]{"6", "a", "b", "a", "b", "a", "1", "8"} // videoteka
////                            new String[]{"5", "8", "11", "9"} // kopija
//                    );
                    newRowIndex = add();
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
        java.util.List<String> values = new ArrayList<>();
        java.util.List<String> columns = new ArrayList<String>();
        for (String s : form.getTableModel().getTableMetaData().getBaseColumnCodes()) {
            columns.add(s);
        }
        for (Component component : form.getDataPanel().getComponents()) {
            if (component instanceof JTextComponent) {
                if (columns.contains(component.getName()))
                    values.add(((JTextComponent) component).getText());
            }
        }

        return form.getTableModel().updateRow(form.getDataTable().getSelectedRow(),
                values.toArray(new String[values.size()])
        );
    }
}
