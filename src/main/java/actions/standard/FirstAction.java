package actions.standard;

import gui.standard.form.Form;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class FirstAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Form form;

	public FirstAction(Form form) {
		putValue(SMALL_ICON,
				new ImageIcon(getClass().getResource("/img/first.gif")));
		putValue(SHORT_DESCRIPTION, "Pocetak");
		this.form = form;
	}

	public void actionPerformed(ActionEvent arg0) {
		int rowCount = form.getTableModel().getRowCount();
		if (rowCount > 0)
			form.getDataTable().setRowSelectionInterval(0, 0);
	}
}
