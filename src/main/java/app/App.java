package app;

import gui.MainFrame;
import meta.JsonHelper;
import meta.mainframe.MainFrameMetaData;

import javax.swing.*;

public class App {
	public static void main(String[] args) throws ClassNotFoundException,
			UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
		UIManager.put("OptionPane.yesButtonText", "Da");
		UIManager.put("OptionPane.noButtonText", "Ne");
		UIManager.put("OptionPane.cancelButtonText", "Otkaži");
//		UIManager.put("OptionPane.okButtonText", "Potvrdi");

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		MainFrameMetaData mfmt = JsonHelper.unmarshall(MainFrameMetaData.LOCATION, MainFrameMetaData.class);
		MainFrame mf = new MainFrame(mfmt);
		mf.setVisible(true);
	}
}
