package actions;

import gui.standard.ColumnValue;
import gui.standard.form.Form;
import gui.standard.form.misc.ColumnData;
import gui.standard.form.misc.StatementExecutor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gui.standard.form.misc.StatementExecutor.STRING;


public class CancelAccountAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    private static final String TITLE = "Ukidanje računa";
    private static final String QUESTION = "Unesite broj računa na koji će sredstva biti preneta :";

    private static final String CANCEL_ACCOUNT_PROCEDURE_CALL = "{ ur(?, ?)}";
    private static final String ACCOUNT2 = "BAR_RACUN2";

    private Form form;

    public CancelAccountAction(Form form) {
        // TODO set icon
//        putValue(SMALL_ICON,
//                new ImageIcon(getClass().getResource("/img/search.gif")));
        putValue(SHORT_DESCRIPTION, TITLE);
        putValue(NAME, TITLE);

        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO form.getSelectAccountNumber
        ColumnData columnData = null;
        String value1 = null;

        String value2 = JOptionPane.showInputDialog(form, QUESTION, TITLE, JOptionPane.QUESTION_MESSAGE);

        if (value2 != null) {
            //TODO validate value2

            StatementExecutor executor;

            List<ColumnValue> values = new ArrayList<>();
            values.add(new ColumnValue(columnData.getCode(), value1));
            values.add(new ColumnValue(ACCOUNT2, value2));

            Map<String, String> columnCodeTypes = new HashMap<>();
            columnCodeTypes.put(columnData.getCode(), columnData.getClassName());
            columnCodeTypes.put(ACCOUNT2, STRING);

            executor = new StatementExecutor(columnCodeTypes);

            try {
                executor.executeProcedure(CANCEL_ACCOUNT_PROCEDURE_CALL, values);
            } catch (SQLException e1) {
                e1.printStackTrace(); // TODO handle error
            }
        }
    }

    private void search() throws SQLException {
        List<String> values = form.getDataPanel().getValues();
        form.getTableModel().search(values.toArray(new String[values.size()]));
    }
}
