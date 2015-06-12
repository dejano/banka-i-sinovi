package controller.action;

import controller.DialogController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SearchAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private DialogController standardForm;

	public SearchAction(DialogController standardForm) {
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
