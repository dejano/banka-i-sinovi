package controller;

import model.TableModel;
import model.db.SchemaRow;
import view.DetailsPanel;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class PanelController implements Observer {
    private final SchemaRow model;
    private TableModel tableModel;
    private final DetailsPanel view;


    public PanelController(SchemaRow model, TableModel tableModel) {
        this.model = model;
        this.tableModel = tableModel;
        this.view = new DetailsPanel(model);
        this.view.addListener(this);
        this.model.addObserver(this.view);
    }

    public DetailsPanel getView() {
        return view;
    }

    public void updateWithZoomData(Map<String, String> zoomData) {
        for (Map.Entry<String, String> entry : zoomData.entrySet()) {
            for (Component component : view.getDataPanel().getComponents()) {
                if (component instanceof JTextComponent && component.getName().equals(entry.getKey())) {
                    JTextComponent textComponent = (JTextComponent) component;
                    textComponent.setText(entry.getValue());
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        int index = (int) arg;
        if (index < 0)
            return;
        for (Component component : view.getDataPanel().getComponents()) {
            if (component instanceof JTextComponent) {
                JTextComponent textComponent = (JTextComponent) component;
                String value = tableModel.getColumnValueByCode(index, textComponent.getName());
                textComponent.setText(value);
            }
        }
    }



}
