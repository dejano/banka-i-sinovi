package view;

import controller.ButtonAction;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class MainFrame extends JFrame implements AbstractView {

    private final List<Object> listeners;
    private Map<String, String> menuItemNames;

    public MainFrame(String title, Map<String, String> menuItemNames) {
        super(title);
        listeners = new ArrayList<Object>();
        this.menuItemNames = menuItemNames;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createGui();
    }

    /**
     * Allow an object to subscribe for UI events.
     *
     * @param o The subscriber object.
     */
    public void addListener(Object o) {
        if (!this.listeners.contains(o)) {
            this.listeners.add(o);
        }
    }

    public void createGui() {

        for (Map.Entry<String, String> entry : menuItemNames.entrySet()) {
            JButton btn1 = new JButton();
            add(btn1);
            btn1.setAction(new ButtonAction(entry.getValue(), entry.getKey(), listeners));
        }

        setLayout(new MigLayout());
    }

    public void showGui() {
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Called by model
     */
    public void update(Observable o, Object arg) {
        System.out.println(arg);
        System.out.println("MainWindow.update");
    }

}
