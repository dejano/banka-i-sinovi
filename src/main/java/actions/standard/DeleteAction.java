package actions.standard;

import gui.dialog.Toast;
import gui.standard.form.Form;
import messages.QuestionMessages;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

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
		if (JOptionPane.showConfirmDialog(form, QuestionMessages.DELETE_ROW, QuestionMessages.TITLE,
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

			try {
				form.removeRow();

				Toast.show(form, "Slog uspešno obrisan.");
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(form, ex.getMessage(), "Greska",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
