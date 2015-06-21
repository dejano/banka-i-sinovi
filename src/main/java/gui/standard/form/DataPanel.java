package gui.standard.form;

import actions.standard.ZoomFormAction;
import gui.standard.form.components.ComponentCreator;
import gui.standard.form.misc.ColumnData;
import gui.standard.form.misc.FormData;
import meta.FormMetaData;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.List;
import java.util.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.*;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.LOOKUP;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.LOOKUP_INSERT;

public class DataPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    //    private FormData formData;
    private Form parent;
    private Map<String, JComponent> inputs = new LinkedHashMap<>();

    public DataPanel(Form parent, FormData formData, boolean readOnlyForm) {
        this.parent = parent;

        this.setLayout(new MigLayout("gapx 15px"));

        initInputs(formData, readOnlyForm);
    }

    private void initInputs(FormData formData, boolean readOnlyForm) {
        Map<String, ColumnData> columns = formData.getColumns(ALL);

        for (ColumnData columnData : columns.values()) {
            if (!columnData.isHiddenInput()) {
                JLabel label = new JLabel(columnData.getName());

                JTextField textField = ComponentCreator.getComponent(columnData);
                textField.setEditable(!readOnlyForm);
                textField.setName(columnData.getCode());

                this.inputs.put(columnData.getCode(), textField);

                if (formData.isReadOnly()
                        || (formData.isInGroup(columnData.getCode(), LOOKUP, PRIMARY_KEYS, NEXT)
                        && !formData.isInGroup(columnData.getCode(), LOOKUP_INSERT))
                        || formData.getZoomBaseColumns().contains(columnData.getCode())) {
                    textField.setEditable(false);
                }

                this.add(label);
                this.add(textField, "wrap");

                if (formData.getZoomBaseColumns().contains(columnData.getCode())) {
                    if (!readOnlyForm) {
                        JButton zoomBtn = new JButton(new ZoomFormAction(parent,
                                formData.getZoomTableCode(columnData.getCode())));
                        this.add(zoomBtn);
                    }
                }
            }
        }
    }

    public java.util.List<String> getValues() {
        java.util.List<String> ret = new ArrayList<>();

        for (String columnCode : parent.getFormData().getColumnCodes(ALL)) {
            JComponent component = inputs.get(columnCode);

            if (component != null && component instanceof JTextComponent) {
                ret.add(((JTextComponent) component).getText()); // TODO handle other component types
            } else {
                String defaultValue = parent.getFormData().getDefaultValue(columnCode);

                if (defaultValue != null)
                    ret.add(defaultValue);
                else
                    ret.add(parent.getFormData().getNextValue(columnCode));
            }
        }

        return ret;
    }

    public void setValue(String columnCode, String value, boolean editable) {
        JTextComponent component = (JTextComponent) inputs.get(columnCode);
        if (component != null) {
            component.setText("");
            component.setText(value);
            component.setEditable(editable);
        }
    }

    public void setBlankEditableInputs() {
        for (String columnCode : inputs.keySet()) {
            JTextComponent component = (JTextComponent) inputs.get(columnCode);
            if (component != null) {
                component.setText("");
                component.setEditable(true);
            }
        }
    }

    public void setBlankEditableInput(String columnCode) {
        JTextComponent component = (JTextComponent) inputs.get(columnCode);
        if (component != null) {
            component.setText("");
            component.setEditable(true);
        }
    }

    public void clearDisableInputs() {
        for (JComponent component : inputs.values()) {
            JTextComponent textComponent = (JTextComponent) component;
            textComponent.setText("");
            textComponent.setEditable(false);
        }
    }

    public Map<String, JComponent> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, JComponent> inputs) {
        this.inputs = inputs;
    }
}
