package actions;

import app.AppData;
import gui.dialog.ErrorMessageDialog;
import gui.standard.ColumnValue;
import gui.standard.form.Form;
import gui.standard.form.misc.ColumnData;
import gui.standard.form.misc.FormData;
import gui.standard.form.misc.StatementExecutor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.PRIMARY_KEYS;
import static gui.standard.form.misc.StatementExecutor.STRING;


public class CancelAccountAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    private static final String TITLE = "Ukidanje računa";
    private static final String QUESTION = "Unesite broj računa na koji će sredstva biti preneta :";

    private static final String CANCEL_ACCOUNT_PROCEDURE_CALL = "{ call cancelBankAccount(?,?,?)}";
    private static final String PIB = "PIB";
    private static final String ACCOUNT2 = "BAR_RACUN2";

    private Form form;

    public CancelAccountAction(Form form) {
        // TODO set icon
        putValue(SHORT_DESCRIPTION, TITLE);
        putValue(NAME, TITLE);

        this.form = form;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Map<String, ColumnData> pks = form.getFormData().getColumnsMap(PRIMARY_KEYS);
        ColumnData columnData = pks.get("BAR_RACUN");
        String value1 = form.getSelectedRowValue("BAR_RACUN");

        String value2 = JOptionPane.showInputDialog(form, QUESTION, TITLE, JOptionPane.QUESTION_MESSAGE);

        if (value2 != null) {
            StatementExecutor executor;

            List<ColumnValue> values = new ArrayList<>();
            values.add(new ColumnValue(PIB, AppData.getInstance().getValue(AppData.AppDataEnum.PIB_BANKE)));
            values.add(new ColumnValue(columnData.getCode(), value1));
            values.add(new ColumnValue(ACCOUNT2, value2));

            Map<String, String> columnCodeTypes = new HashMap<>();
            columnCodeTypes.put(PIB, StatementExecutor.STRING);
            columnCodeTypes.put(columnData.getCode(), columnData.getClassName());
            columnCodeTypes.put(ACCOUNT2, STRING);

            executor = new StatementExecutor(columnCodeTypes);

            try {
                executor.executeProcedure(CANCEL_ACCOUNT_PROCEDURE_CALL, values);
            } catch (SQLException e1) {
                ErrorMessageDialog.show(form, e1);
            }
        }
    }

}
