package actions.standard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class DeleteAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;
	
	public DeleteAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/remove.gif")));
		putValue(SHORT_DESCRIPTION, "Brisanje");
		this.standardForm=standardForm;
		
	}

	public void actionPerformed(ActionEvent arg0) {
//		if(standardForm instanceof DrzavaStandardForm){
//			if (JOptionPane.showConfirmDialog(standardForm,
//					"Da li ste sigurni da zelite da obrisete drzavu " + ((DrzavaStandardForm) standardForm).getTblGrid().getValueAt(((DrzavaStandardForm) standardForm).getTblGrid().getSelectedRow(), 1) + "?", "Pitanje",
//					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//				int br = ((DrzavaStandardForm) standardForm).getTblGrid().getSelectedRow();
//				try {
//					((DrzavaStandardForm) standardForm).getTableModel().deleteRow(br);
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
	}
}
