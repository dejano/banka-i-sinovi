package actions.standard;

import gui.standard.form.Form;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class PickupAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private Form standardForm;
	
	public PickupAction(Form standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/zoom-pickup.gif")));
		putValue(SHORT_DESCRIPTION, "Zoom pickup");
		this.standardForm = standardForm;
		
	}

	public void actionPerformed(ActionEvent arg0) {
		standardForm.setVisible(false);
		standardForm.dispose();
	}
}
