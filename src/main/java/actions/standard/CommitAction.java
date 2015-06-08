package actions.standard;

import gui.standard.form.misc.ErrorMessages;
import gui.standard.form.Form;
import gui.standard.form.StatusBar.FormModeEnum;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;


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
		FormModeEnum mode = form.getMode();

		try {
			int newRowIndex = -1;

			switch (mode) {
			case ADD:
				newRowIndex = form.getTableModel().insertRow(new String []{"4", "a", "a", "a", "a", "a", "7", "4"});

				break;
			case EDIT:
	//				form.getTableModel().updateRow(form.getDataPanel().getValues());
				newRowIndex = form.getTableModel().updateRow(form.getDataTable().getSelectedRow(),
						new String []{"5", "a", "b", "a", "b", "a", "1", "4"});

				break;
			default:
				break;
			}

			if(newRowIndex != -1)
				form.getDataTable().setRowSelectionInterval(newRowIndex, newRowIndex);
		} catch (SQLException exception) {
			if(exception.getErrorCode() == ErrorMessages.CUSTOM_ERROR_CODE){
				JOptionPane.showConfirmDialog(form, exception.getMessage(), "Greska",
						JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
			} else {
				exception.printStackTrace();
			}
		}
	}
}
