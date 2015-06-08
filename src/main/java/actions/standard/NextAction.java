package actions.standard;

import gui.standard.form.Form;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class NextAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Form form;

	public NextAction(Form form) {
		putValue(SMALL_ICON,
				new ImageIcon(getClass().getResource("/img/next.gif")));
		putValue(SHORT_DESCRIPTION, "Sledeci");
		this.form = form;

	}

	public void actionPerformed(ActionEvent arg0) {
		int br = form.getDataTable().getSelectedRow();
		int rowCount = form.getTableModel()
				.getRowCount();
		
		if (rowCount > 0) {
			if (br < rowCount - 1) {
				form.getDataTable()
						.setRowSelectionInterval(br + 1, br + 1);
			} 
		}
	}
}
