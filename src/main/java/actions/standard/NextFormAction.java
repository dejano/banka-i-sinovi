package actions.standard;

import gui.standard.ColumnMapping;
import gui.standard.form.Form;
import messages.WarningMessages;
import meta.NextMetaData;
import meta.FormCreator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class NextFormAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private static final String CHOOSE_NEXT_FORM_MESSAGE = "Selektujte sledecu formu";
    private static final String CHOOSE_NEXT_FORM_TITLE = "Sledeća forma";
    private Form form;

    public NextFormAction(Form form) {
        putValue(SMALL_ICON,
                new ImageIcon(getClass().getResource("/img/nextform.gif")));
        putValue(SHORT_DESCRIPTION, "Sledeća forma");
        this.form = form;

    }

    public void actionPerformed(ActionEvent e) {
        int nextFormsCount = form.getNextData().size();

        NextMetaData nextData = null;
        switch (nextFormsCount) {
            case 1:
                nextData = form.getNextData().get(0);
                break;
            default:
                nextData = showNextFormSelectionDialog();

                break;
        }

        if (nextData != null) {
            try {
                createNextForm(nextData);
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

    private void createNextForm(NextMetaData nextData) throws SQLException {
        Map<String, String> nextValues = new HashMap<>();
        for (ColumnMapping columnCodeMappingEntry : nextData.getColumnCodeMapping()) {
            nextValues.put(columnCodeMappingEntry.getTo(),
                    form.getSelectedRowValue(columnCodeMappingEntry.getFrom()));
        }

        // TODO check if exists column code in next form
        Map<String, String> currFormNextValues = form.getTableModel().getNextColumnCodeValues();
        if (currFormNextValues != null)
            nextValues.putAll(currFormNextValues);

        Form nextForm = FormCreator.getNextStandardForm(nextData.getFormName(), nextValues);
        nextForm.setVisible(true);
    }

    private NextMetaData showNextFormSelectionDialog() {
        NextMetaData ret = null;

        String[] nextFormNames = new String[form.getNextData().size()];
        int i = 0;
        for (NextMetaData nd : form.getNextData()) {
            nextFormNames[i++] = nd.getFormName();
        }

        String selectedNextFormName = (String) JOptionPane.showInputDialog(form,
                CHOOSE_NEXT_FORM_MESSAGE,
                CHOOSE_NEXT_FORM_TITLE,
                JOptionPane.QUESTION_MESSAGE,
                null,
                nextFormNames,
                nextFormNames[0]);

        if (selectedNextFormName != null) {
            for (NextMetaData nd : form.getNextData()) {
                if (selectedNextFormName.equals(nd.getFormName())) {
                    ret = nd;
                    break;
                }
            }
        }

        return ret;
    }

}
