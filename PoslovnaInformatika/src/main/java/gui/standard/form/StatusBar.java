package gui.standard.form;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;

	public enum FormModeEnum {
		DEFAULT, ADD, EDIT, SEARCH
	}

	private FormModeEnum mode;
	private JLabel statusText;

	public StatusBar() {
		statusText = new JLabel();
		setMode(FormModeEnum.DEFAULT);
		this.add(statusText);
	}

	public FormModeEnum getMode() {
		return mode;
	}

	public void setMode(FormModeEnum mode) {
		String text = null;

		this.mode = mode;

		switch (mode) {
		case ADD:
			text = "Dodavanje novog sloga";
			break;
		case EDIT:
			text = "Izmena sloga";
			break;
		case SEARCH:
			text = "Pretraga";
			break;
		case DEFAULT:
			text = " ";
			break;
		}

		statusText.setText(text);
	}

}
