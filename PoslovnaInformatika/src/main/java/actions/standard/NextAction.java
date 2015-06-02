package actions.standard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class NextAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;

	public NextAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/next.gif")));
		putValue(SHORT_DESCRIPTION, "Sledeci");
		this.standardForm=standardForm;

	}

	
	public void actionPerformed(ActionEvent arg0) {
//		if(standardForm instanceof DrzavaStandardForm){
//			int br = ((DrzavaStandardForm) standardForm).getTblGrid().getSelectedRow();
//			int rowCount = ((DrzavaStandardForm) standardForm).getTableModel().getRowCount(); 
//		    if (rowCount > 0){
//		    	if(br < rowCount-1){
//		    		((DrzavaStandardForm) standardForm).getTblGrid().setRowSelectionInterval(br+1, br+1);
//		    		br++;
//		    	}else return;
//		    	
//		    }
//		}
	}
}
