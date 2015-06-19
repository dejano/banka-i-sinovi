package gui.standard.form;

import actions.standard.ZoomFormAction;
import gui.standard.form.components.ComponentCreator;
import gui.standard.form.misc.ColumnData;
import gui.standard.form.misc.FormData;
import meta.FormMetaData;
import net.miginfocom.swing.MigLayout;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;

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
    
    private void initInputs(FormData formData, boolean readOnlyForm){
        Map<String, ColumnData> columns = formData.getColumns(ALL);

        for (ColumnData columnData : columns.values()) {
            if (!columnData.isHiddenInput()) {
                JLabel label = new JLabel(columnData.getName());

                JTextField textField = ComponentCreator.getComponent(columnData);
                textField.setEditable(!readOnlyForm);
                textField.setName(columnData.getCode());

                if ((formData.isInGroup(columnData.getCode(), LOOKUP, PRIMARY_KEYS, NEXT)
                        && !formData.isInGroup(columnData.getCode(), LOOKUP_INSERT))
                        || formData.getZoomBaseColumns().contains(columnData.getCode())) {
                    textField.setEditable(false);
                }

                if (formData.getZoomBaseColumns().contains(columnData.getCode())) {
                    this.add(label);
                    this.add(textField, "wrap");

                    if (!readOnlyForm) {
                        JButton zoomBtn = new JButton(new ZoomFormAction(parent,
                                formData.getZoomTableCode(columnData.getCode())));
                        this.add(zoomBtn);
                    }
                } else if (formData.isInGroup(columnData.getCode(), LOOKUP)) {
                    this.add(label);
                    this.add(textField, "wrap");
                } else {
                    this.add(label);
                    this.add(textField, "wrap");
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
