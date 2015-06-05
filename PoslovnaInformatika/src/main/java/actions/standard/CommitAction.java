package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.StatusBar.FormStatusEnum;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class CommitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Form form;

	public CommitAction(Form form) {
		putValue(SMALL_ICON,
				new ImageIcon(getClass().getResource("/img/commit.gif")));
		putValue(SHORT_DESCRIPTION, "Commit");
		this.form = form;
	}

	public void actionPerformed(ActionEvent e) {
		FormStatusEnum mode = form.getMode();

		switch (mode) {
		case ADD:
			try {
				// form.getTableModel().insertRow(form.getDataPanel().getValues());
				form.getTableModel()
						.insertRow(
								new String[] { "4", "a", "a", "a", "a", "a",
										"1", "2" });
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			break;
		case EDIT:
			try {
				// form.getTableModel().updateRow(form.getDataTable().getSelectedRow(),
				// form.getDataPanel().getValues());
				form.getTableModel()
						.updateRow(
								form.getDataTable().getSelectedRow(),
								new String[] { "4", "a", "b", "a", "b", "a",
										"1", "3" });
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			break;
		default:
			break;
		}
	}
}
