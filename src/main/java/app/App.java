package app;

import gui.MainFrame;
import gui.dialog.BankSelectionDialog;
import gui.standard.ColumnValue;
import meta.JsonHelper;
import meta.mainframe.MainFrameMetaData;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class App {

    private static MainFrame mainFrame;

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    public static void main(String[] args) throws ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.put("OptionPane.yesButtonText", "Da");
        UIManager.put("OptionPane.noButtonText", "Ne");
        UIManager.put("OptionPane.cancelButtonText", "Otkaži");
//		UIManager.put("OptionPane.okButtonText", "Potvrdi");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        setUIFont(new javax.swing.plaf.FontUIResource("Segoe ", Font.PLAIN, 12));

        String bankPib = BankSelectionDialog.show();
        AppData.getInstance().put(AppData.AppDataEnum.PIB_BANKE, bankPib);

        if (bankPib != null) {
            MainFrameMetaData mfmt = JsonHelper.unmarshall(MainFrameMetaData.LOCATION, MainFrameMetaData.class);
            mainFrame = new MainFrame(mfmt);
            mainFrame.setVisible(true);
        }
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
}
