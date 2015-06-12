package view;

import controller.ButtonAction;
import model.db.SchemaColumn;
import model.db.SchemaRow;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;

public class DetailsPanel extends JPanel implements AbstractPanel {

    private final java.util.List<Object> listeners;

    private JPanel dataPanel;

    public DetailsPanel(SchemaRow schemaRow) {
        listeners = new ArrayList<Object>();
        createGui(schemaRow);
    }

    @Override
    public void createGui(SchemaRow schemaRow) {
        setLayout(new MigLayout("fillx"));
        dataPanel = new JPanel();
        dataPanel.setLayout(new MigLayout("gapx 15px"));
        JLabel spacer;

        for (final SchemaColumn column : schemaRow.getSchemaColumns()) {

            JLabel label = new JLabel(column.getName());
            JTextField textField = new JTextField(40);
            textField.setName(column.getCode());
            if (schemaRow.getPrimaryKeys().contains(column)) {
//                dataPanel.add(spacer = new JLabel(" "),"span, grow");
                textField.setEditable(false);
            }

            if (schemaRow.getForeignKeys().containsKey(column)) {
                dataPanel.add(label);
                dataPanel.add(textField);

                JButton button = new JButton(new ButtonAction("...", column.getReferencedTableName(), listeners));
                dataPanel.add(button);
            } else if (schemaRow.getForeignKeys().containsValue(column)) {
                dataPanel.add(textField, "wrap");
            } else {
                dataPanel.add(label);
                dataPanel.add(textField, "wrap");
            }
        }
        add(dataPanel);

        JPanel buttonsPanel = new JPanel();
        JButton btnCommit = new JButton("commit");
        JButton btnRollback = new JButton("roolback");
        buttonsPanel.setLayout(new MigLayout("wrap"));
        buttonsPanel.add(btnCommit);
        buttonsPanel.add(btnRollback);
        add(buttonsPanel, "dock east");
    }

    @Override
    public void createGui() {

    }

    @Override
    public void showGui() {
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

    }

    public JPanel getDataPanel() {
        return dataPanel;
    }
}
