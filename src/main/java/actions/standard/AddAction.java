package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.StatusBar.FormModeEnum;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.BASE;

public class AddAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    // kada se napravi genericka forma, staviti tu klasu umesto JDialog
    private Form form;

    public AddAction(Form form) {
        putValue(SMALL_ICON,
                new ImageIcon(getClass().getResource("/img/add.gif")));
        putValue(SHORT_DESCRIPTION, "Dodavanje");
        this.form = form;
    }

    public void actionPerformed(ActionEvent e) {
        form.setMode(FormModeEnum.ADD);
        form.getDataTable().clearSelection();
    }
}
