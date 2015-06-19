package actions.standard;

import gui.standard.form.Form;
import gui.standard.form.StatusBar;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.ALL;

public class SearchAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private Form form;

    public SearchAction(Form form) {
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/search.gif")));
        putValue(SHORT_DESCRIPTION, "Pretraga");
        this.form = form;
    }

    public void actionPerformed(ActionEvent e) {
        form.setMode(StatusBar.FormModeEnum.SEARCH);

        // TODO move to DataPanel?
        for (Component component : form.getDataPanel().getComponents()) {
            if (component instanceof JTextComponent) {
                if (form.getFormData().isInGroup(component.getName(), ALL)) {
                    ((JTextComponent) component).setEditable(true);
                }
                ((JTextComponent) component).setText("");
            }
        }

        // TODO move to button next to commit and rollback
    }
}
