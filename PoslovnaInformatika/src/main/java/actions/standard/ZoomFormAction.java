package actions.standard;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

public class ZoomFormAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;

	public ZoomFormAction(JDialog standardForm) {
		putValue(SHORT_DESCRIPTION, "Zoom");
		putValue(NAME, "...");
		this.standardForm = standardForm;
	}

	public void actionPerformed(ActionEvent event) {
//		if (this.standardForm instanceof NaseljenoMestoStandardForm) {
//			NaseljenoMestoStandardForm nmForm = (NaseljenoMestoStandardForm) standardForm;
//
//			DrzavaStandardForm dForm;
//			try {
//				dForm = new DrzavaStandardForm();
//				dForm.enablePickup();
//
//				dForm.setVisible(true);
//				
//				ColumnList zoomList = dForm.getColumnList();
//				
//				if(zoomList != null)
//					nmForm.setZoomPickup(zoomList);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
