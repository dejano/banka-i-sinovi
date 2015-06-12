package view;

import controller.DialogController;
import controller.action.SearchAction;
import controller.action.ToolBarAction;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ToolBar extends JToolBar {

    private final JButton btnPickup;
    private final List<Object> listeners;
    private final JButton btnNext;
    private final JButton btnDelete;

    public ToolBar(DialogController controller) {
        listeners = new ArrayList<Object>();

        JButton btnSearch = new JButton(new SearchAction(controller));
        add(btnSearch);

        JButton btnRefresh = new JButton(new ToolBarAction(new ImageIcon(getClass().getResource("/img/refresh.gif")), "Refresh", "refresh", listeners));
        add(btnRefresh);

        btnPickup = new JButton(new ToolBarAction(new ImageIcon(getClass().getResource("/img/zoom-pickup.gif")), "Zoom", "pickup", listeners));
        btnPickup.setEnabled(false);
        add(btnPickup);

        btnNext = new JButton(new ToolBarAction(new ImageIcon(getClass().getResource("/img/next.gif")), "Next", "next", listeners));
        btnNext.setEnabled(false);
        add(btnNext);

        btnDelete = new JButton(new ToolBarAction(new ImageIcon(getClass().getResource("/img/remove.gif")), "Remove", "remove", listeners));
        btnDelete.setEnabled(false);
        add(btnDelete);
    }

    public void addListener(Object o) {
        if (!this.listeners.contains(o)) {
            this.listeners.add(o);
        }
    }

    public JButton getBtnPickup() {
        return btnPickup;
    }

    public JButton getBtnNext() {
        return btnNext;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }
}
