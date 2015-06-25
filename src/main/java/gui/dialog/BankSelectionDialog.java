package gui.dialog;

import gui.standard.form.misc.StatementExecutor;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 17.6.2015..
 */
public class BankSelectionDialog {

    private static final String TITLE = "Selekcija banke";
    private static final String LABEL = "Unesite naziv banke: ";

    private static final String GET_BANKS_QUERY = "SELECT PR_NAZIV, PR_PIB FROM PRAVNA_LICA " +
            "WHERE PR_BANKA=1 ORDER BY PR_NAZIV";

    public static String show() {
        String ret = null;

        StatementExecutor executor = new StatementExecutor();

        List<String> columnCodes = new ArrayList<>();
        columnCodes.add("PR_NAZIV");
        columnCodes.add("PR_PIB");

        List<String[]> bankResults = new ArrayList<>();
        try {
            bankResults = executor.execute(GET_BANKS_QUERY, columnCodes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] bankNames = new String[bankResults.size()];
        int i = 0;
        for (String[] result : bankResults) {
            bankNames[i++] = result[0];
        }

        JPanel myPanel = new JPanel(new MigLayout());
        myPanel.add(new JLabel(LABEL));
        JComboBox comboBox = new JComboBox(bankNames);
        myPanel.add(comboBox);

        AutoCompleteDecorator.decorate(comboBox);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                TITLE, JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedBankName = (String) comboBox.getSelectedItem();

            for (String[] bankResult : bankResults) {
                if (bankResult[0].equals(selectedBankName)) {
                    ret = bankResult[1];
                    break;
                }
            }
        }

        return ret;
    }

    private BankSelectionDialog() {
    }
}
