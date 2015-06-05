package gui.standard.form;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusBar extends JPanel {

	private static final long serialVersionUID = 1L;

	public enum FormStatusEnum {
		ADD, EDIT, SEARCH
	}

	private FormStatusEnum status;
	private JLabel statusText;

	public StatusBar() {
		statusText = new JLabel();
		setStatus(FormStatusEnum.EDIT);
		this.add(statusText);
	}

	public FormStatusEnum getStatus() {
		return status;
	}

	public void setStatus(FormStatusEnum status) {
		String text = null;

		this.status = status;

		switch (status) {
		case ADD:
			text = "Dodavanje novog sloga";
			break;
		case EDIT:
			text = "Izmena sloga";
			break;
		case SEARCH:
			text = "Pretraga";
			break;
		}

		statusText.setText(text);
	}

}
