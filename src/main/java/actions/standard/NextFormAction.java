package actions.standard;

import gui.standard.ColumnMapping;
import gui.standard.form.Form;
import meta.NextMetaData;
import meta.StandardFormCreator;

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

        if(nextData != null)
            createNextForm(nextData);
    }

    private void createNextForm(NextMetaData nextData) {
        Map<String, String> nextValues = new HashMap<>();
        for (ColumnMapping columnCodeMappingEntry : nextData.getColumnCodeMapping()) {
            nextValues.put(columnCodeMappingEntry.getColumnCode2(),
                    form.getSelectedRowValue(columnCodeMappingEntry.getColumnCode1()));
        }

        // TODO check if exists column code in next form
        Map<String, String> currFormNextValues = form.getTableModel().getNextColumnCodeValues();
        if (currFormNextValues != null)
            nextValues.putAll(currFormNextValues);

        try {
            Form nextForm = StandardFormCreator.getNextStandardForm(nextData.getFormName(), nextValues);
            nextForm.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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