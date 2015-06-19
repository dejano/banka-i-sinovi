package app;

import gui.MainFrame;
import gui.dialog.BankSelectionDialog;
import gui.standard.ColumnValue;
import meta.JsonHelper;
import meta.mainframe.MainFrameMetaData;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public class App {

    private static MainFrame mainFrame;

    public static MainFrame getMainFrame(){
        return  mainFrame;
    }

    public static void main(String[] args) throws ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.put("OptionPane.yesButtonText", "Da");
        UIManager.put("OptionPane.noButtonText", "Ne");
        UIManager.put("OptionPane.cancelButtonText", "Otkaži");
//		UIManager.put("OptionPane.okButtonText", "Potvrdi");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        String bankPib = BankSelectionDialog.show();
        AppData.getInstance().put("pib_banke", bankPib);

        if (bankPib != null) {
            MainFrameMetaData mfmt = JsonHelper.unmarshall(MainFrameMetaData.LOCATION, MainFrameMetaData.class);
            mainFrame = new MainFrame(mfmt);
            mainFrame.setVisible(true);
        }
    }
}
