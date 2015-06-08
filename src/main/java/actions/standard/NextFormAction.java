package actions.standard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class NextFormAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;

	public NextFormAction(JDialog standardForm) {
		putValue(SMALL_ICON,
				new ImageIcon(getClass().getResource("/img/nextform.gif")));
		putValue(SHORT_DESCRIPTION, "Sledeća forma");
		this.standardForm = standardForm;

	}

	public void actionPerformed(ActionEvent e) {
//		if (standardForm instanceof DrzavaStandardForm) {
//			((DrzavaStandardForm) standardForm).createNextList();
//			String whereClause = ((DrzavaStandardForm) standardForm)
//					.getColumnList().getWhereClause();
////			NaseljenoMestoStandardForm nmForm = new NaseljenoMestoStandardForm(
////					whereClause);
////			nmForm.setVisible(true);
//		}
	}
}
