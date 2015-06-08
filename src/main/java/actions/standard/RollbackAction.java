
package actions.standard;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RollbackAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private JDialog standardForm;

    public RollbackAction(JDialog standardForm) {
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/remove.gif")));
        putValue(SHORT_DESCRIPTION, "Poništi");
        this.standardForm = standardForm;
    }

    public void actionPerformed(ActionEvent e) {

    }
}
