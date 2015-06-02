package actions.standard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class SearchAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;

	public SearchAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/search.gif")));
		putValue(SHORT_DESCRIPTION, "Pretraga");
		this.standardForm=standardForm;
	}

	public void actionPerformed(ActionEvent e) {
//		if(standardForm instanceof DrzavaStandardForm){
//			((DrzavaStandardForm) standardForm).setMode(3);
//			((DrzavaStandardForm) standardForm).getTfSifra().setText("");
//			((DrzavaStandardForm) standardForm).getTfNaziv().setText("");
//			((DrzavaStandardForm) standardForm).getTfSifra().requestFocus();
//			
//			try {
//				((DrzavaStandardForm) standardForm).getTableModel().search(((DrzavaStandardForm) standardForm).getTfSifra().getText(), ((DrzavaStandardForm) standardForm).getTfNaziv().getText());
//			} catch (SQLException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
	}
}
