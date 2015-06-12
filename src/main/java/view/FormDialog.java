package view;

import model.db.SchemaRow;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.util.*;

/**
 * Created by dejan on 6/9/2015.
 */
public class FormDialog extends JDialog implements AbstractDialog {

    private final java.util.List<Object> listeners;
    private ToolBar toolBar;
    private Table table;
    private DetailsPanel detailsPanel;

    public FormDialog(Frame owner, String title) {
        super(owner, title);
        setModal(true);
        listeners = new ArrayList<Object>();
        createGui();
    }

    @Override
    public void createGui() {
        setLayout(new MigLayout("fill"));
    }

    @Override
    public void showGui() {
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void addListener(Object o) {
        if (!this.listeners.contains(o)) {
            this.listeners.add(o);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("FormDialog.update");
    }

    @Override
    public void setToolBar(ToolBar toolBar) {
        this.toolBar = toolBar;
        add(toolBar, "dock north");
    }

    @Override
    public void setTable(Table table) {
        this.table = table;
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, "grow, wrap");
    }

    @Override
    public void setPanel(DetailsPanel detailsPanel) {
        this.detailsPanel = detailsPanel;
        add(detailsPanel, "grow, wrap");
    }
}
