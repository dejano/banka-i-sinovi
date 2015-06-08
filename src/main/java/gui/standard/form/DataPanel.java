package gui.standard.form;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

// TODO implement
public class DataPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Map<String, JComponent> inputs = new LinkedHashMap<String, JComponent>();

    public String getValue(String columnCode) {
        return null;
    }

    public String[] getValues() {
        return null;
    }

    public Map<String, JComponent> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, JComponent> inputs) {
        this.inputs = inputs;
    }
}
