package actions.standard;

import gui.standard.form.Form;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class LastAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Form form;

	public LastAction(Form form) {
		putValue(SMALL_ICON,
				new ImageIcon(getClass().getResource("/img/last.gif")));
		putValue(SHORT_DESCRIPTION, "Poslednji");
		this.form = form;
	}

	public void actionPerformed(ActionEvent arg0) {
		int rowCount = form.getTableModel().getRowCount();

		if (rowCount > 0)
			form.getDataTable().setRowSelectionInterval(rowCount - 1,
					rowCount - 1);
	}
}
