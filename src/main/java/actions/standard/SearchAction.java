package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.StatusBar;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class SearchAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Form form;

	public SearchAction(Form form) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/search.gif")));
		putValue(SHORT_DESCRIPTION, "Pretraga");
		this.form = form;
	}

	public void actionPerformed(ActionEvent e) {
		form.setMode(StatusBar.FormModeEnum.SEARCH);
		try {
			form.getTableModel().search(new String[]{"", "BHA", "", "", "", "", "", ""});
		} catch (SQLException e1) {
			// TODO show error message
			e1.printStackTrace();
		}
	}
}
