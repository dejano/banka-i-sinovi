package actions.standard;


import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;



public class CommitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;
	
	public CommitAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/commit.gif")));
		putValue(SHORT_DESCRIPTION, "Commit");
		this.standardForm=standardForm;
	}

	public void actionPerformed(ActionEvent e) {
//		if(standardForm instanceof DrzavaStandardForm){
//			try {
//				((DrzavaStandardForm) standardForm).getTableModel().insertRow(((DrzavaStandardForm) standardForm).getTfSifra().getText(), ((DrzavaStandardForm) standardForm).getTfNaziv().getText());
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
	}
}

