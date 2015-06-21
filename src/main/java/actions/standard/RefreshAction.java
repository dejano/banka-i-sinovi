package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.StatusBar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.BASE;

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
			form.setMode(StatusBar.FormModeEnum.DEFAULT);
			form.getTableModel().open();
			form.getDataPanel().setBlankEditableInputs();
			for (String columnCode : form.getFormData().getColumnCodes(BASE)) {
				form.getDataPanel().setBlankEditableInput(columnCode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
