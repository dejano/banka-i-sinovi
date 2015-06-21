package actions;

import app.App;
import app.AppData;
import gui.dialog.BankSelectionDialog;
import gui.dialog.Toast;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Nikola on 19.6.2015..
 */
public class SwitchBankAction extends AbstractAction {

    private static final String TITLE = "Promena banke matora";

    public SwitchBankAction() {
        super(TITLE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String bankPib = BankSelectionDialog.show();
        if(bankPib != null && !AppData.getInstance().getValue(AppData.AppDataEnum.PIB_BANKE).equals(bankPib)) {
            AppData.getInstance().put(AppData.AppDataEnum.PIB_BANKE, bankPib);
            Toast.show(App.getMainFrame(), "Uspešna selekcija banke.");
        }
    }
}
