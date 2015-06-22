package gui.standard.form;

import actions.standard.ZoomFormAction;
import com.toedter.calendar.JDateChooser;
import gui.standard.form.components.ComponentCreator;
import gui.standard.form.misc.ColumnData;
import gui.standard.form.misc.FormData;
import net.miginfocom.swing.MigLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.*;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.LOOKUP;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.LOOKUP_INSERT;
import static util.ValueMapper.NO;
import static util.ValueMapper.YES;

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
        Map<String, ColumnData> columns = formData.getColumnsMap(ALL);

        for (ColumnData columnData : columns.values()) {
            if (!columnData.isHiddenInput()) {
                JLabel label = new JLabel(columnData.getName());

                JComponent component = ComponentCreator.getComponent(columnData);
                component.setName(columnData.getCode());

                this.inputs.put(columnData.getCode(), component);

                if (formData.isReadOnly()
                        || (formData.isInGroup(columnData.getCode(), LOOKUP, PRIMARY_KEYS, NEXT)
                        && !formData.isInGroup(columnData.getCode(), LOOKUP_INSERT))
                        || formData.getZoomBaseColumns().contains(columnData.getCode())) {
                    if (component instanceof JTextComponent)
                        ((JTextComponent) component).setEditable(!readOnlyForm);
                    else
                        component.setEnabled(false);
                }

                this.add(label);
                this.add(component, "wrap");

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

            if (component != null) {
                if (component instanceof JTextComponent) {
                    ret.add(((JTextComponent) component).getText()); // TODO handle other component types
                } else if (component instanceof JCheckBox) {
                    boolean checked = ((JCheckBox) component).isSelected();
                    ret.add(checked ? "1" : "0");
                } else if (component instanceof JDateChooser) {
                    JDateChooser dateChooser = (JDateChooser) component;
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                    ret.add(df.format(dateChooser.getDate()));
                }
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
        JComponent component = inputs.get(columnCode);
        if (component != null) {
            if (component instanceof JTextComponent) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
                textComponent.setText(value);
                textComponent.setEditable(editable);
            } else if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setEnabled(editable);
                switch (value) {
                    case "1":
                    case YES:
                        checkBox.setSelected(true);
                        break;
                    case "0":
                    case NO:
                    default:
                        checkBox.setSelected(false);
                        break;
                }
            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;

                try {
                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy.");
                    dateChooser.setDate(df.parse(value));
                    dateChooser.setEnabled(editable);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setBlankEditableInputs() {
        for (JComponent component : inputs.values()) {
            if (component != null) {
                if (component instanceof JTextComponent) {
                    JTextComponent textComponent = (JTextComponent) component;
                    textComponent.setText("");
                    textComponent.setEditable(true);
                } else if (component instanceof JCheckBox) {
                    ((JCheckBox) component).setSelected(false);
                } else if (component instanceof JDateChooser) {
                    JDateChooser dateChooser = (JDateChooser) component;
                    dateChooser.setDate(null);
                    dateChooser.setEnabled(true);
                }
            }
        }
    }

    public void setBlankEditableInput(String columnCode) {
        JComponent component = inputs.get(columnCode);
        if (component != null) {
            if (component instanceof JTextComponent) {
                JTextComponent textComponent = (JTextComponent) component;
                textComponent.setText("");
                textComponent.setEditable(true);
            } else if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setSelected(false);
                checkBox.setEnabled(true);
            } else if (component instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) component;
                dateChooser.setDate(null);
                dateChooser.setEnabled(true);
            }
        }

    }

    public void clearDisableInputs() {
        for (JComponent component : inputs.values()) {
            if (component != null) {
                if (component instanceof JTextComponent) {
                    JTextComponent textComponent = (JTextComponent) component;
                    textComponent.setText("");
                    textComponent.setEditable(false);
                } else if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    checkBox.setSelected(false);
                    checkBox.setEnabled(false);
                } else if (component instanceof JDateChooser) {
                    JDateChooser dateChooser = (JDateChooser) component;
                    dateChooser.setDate(null);
                    dateChooser.setEnabled(false);
                }
            }
        }
    }

    public Map<String, JComponent> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, JComponent> inputs) {
        this.inputs = inputs;
    }
}
