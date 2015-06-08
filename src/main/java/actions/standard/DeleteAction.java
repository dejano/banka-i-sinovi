package actions.standard;

import gui.standard.form.Form;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class DeleteAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Form form;

	public DeleteAction(Form form) {
		putValue(SMALL_ICON,
				new ImageIcon(getClass().getResource("/img/remove.gif")));
		putValue(SHORT_DESCRIPTION, "Brisanje");
		this.form = form;

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO add to json delete message and column
		if (JOptionPane.showConfirmDialog(form,
				"Da li ste sigurni da zelite da obrisete " + "?", "Pitanje",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

			form.removeRow();
		}
	}
}
