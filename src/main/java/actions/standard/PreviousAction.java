package actions.standard;

import gui.standard.form.Form;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class PreviousAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private Form form;

	public PreviousAction(Form form) {
		putValue(SMALL_ICON,
				new ImageIcon(getClass().getResource("/img/prev.gif")));
		putValue(SHORT_DESCRIPTION, "Prethodni");
		this.form = form;

	}

	public void actionPerformed(ActionEvent arg0) {
		int selectedRowIndex = form.getDataTable().getSelectedRow();
		int rowCount = form.getTableModel().getRowCount();

		if (rowCount > 0) {
			if (selectedRowIndex > 0) {
				form.selectRow(selectedRowIndex - 1);
			} else {
				form.selectRow(rowCount - 1);
			}
		}
	}
}
