package gui.dialog;

import gui.standard.form.misc.StatementExecutor;
import meta.NextMetaData;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 17.6.2015..
 */
public class BankSelectionDialog {

    private static final String BANK_SELECTION_TITLE = "Selekcija banke";
    private static final String BANK_SELECTION_MESSAGE = "Selektujte banku :";

    private static final String GET_BANKS_QUERY = "SELECT TOP 10 PR_NAZIV, PR_PIB FROM PRAVNA_LICA " +
            "WHERE PR_BANKA=1 ORDER BY PR_NAZIV";

    public static String show(){
        String ret = null;

        StatementExecutor executor = new StatementExecutor();

        List<String> columnCodes = new ArrayList<>();
        columnCodes.add("PR_NAZIV");
        columnCodes.add("PR_PIB");

        List<String[]> results = new ArrayList<>();
        try {
            results = executor.execute(GET_BANKS_QUERY, columnCodes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] bankNames = new String[results.size()];
        int i = 0;
        for (String[] result : results) {
            bankNames[i++] = result[0];
        }

        String selectedBankName = (String) JOptionPane.showInputDialog(null,
                BANK_SELECTION_MESSAGE,
                BANK_SELECTION_TITLE,
                JOptionPane.QUESTION_MESSAGE,
                null,
                bankNames,
                bankNames[0]);

        if (selectedBankName != null) {
            for (String[] result : results) {
                if(result[0].equals(selectedBankName)) {
                    ret = result[1];
                    break;
                }
            }
        }

        return ret;
    }

    private BankSelectionDialog(){}
}
