package actions.standard;

import gui.standard.form.Form;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class RefreshAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private Form form;

	public RefreshAction(Form form) {
		putValue(SMALL_ICON,
				new ImageIcon(getClass().getResource("/img/refresh.gif")));
		putValue(SHORT_DESCRIPTION, "Refresh");

		this.form = form;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			form.getTableModel().open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}