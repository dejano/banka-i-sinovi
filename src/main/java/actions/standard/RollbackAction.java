
package actions.standard;

import gui.standard.form.Form;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RollbackAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private Form form;

    public RollbackAction(Form form) {
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/remove.gif")));
        putValue(SHORT_DESCRIPTION, "Poništi");
        this.form = form;
    }

    public void actionPerformed(ActionEvent e) {
        form.sync();
    }
}
