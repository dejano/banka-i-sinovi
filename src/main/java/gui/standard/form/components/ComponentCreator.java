package gui.standard.form.components;

import gui.standard.form.misc.ColumnData;

import javax.swing.*;
import java.sql.Types;

import static gui.standard.form.components.ValidationDatePicker.E_VALID_DATES.ALL;

/**
 * Created by Nikola on 17.6.2015..
 */
public class ComponentCreator {

    // TODO min size
    public static JComponent getComponent(ColumnData columnData) {
        JComponent component = null;
        String className = columnData.getClassName();

        switch (className) {
            case "java.lang.String":
                component = new ValidationTextField(columnData.isMandatory(),
                        className.substring(className.lastIndexOf('.')), columnData.getLength() + 5);
                break;
            case "java.math.BigInteger":
            case "java.math.BigDecimal":
                if(columnData.getPrecision() == 0){
                    component = new ValidationTextField(!columnData.isMandatory(),
                            Types.BIGINT, columnData.getLength(), columnData.getLength());
                } else {
                    component = new DecimalTextField(columnData.getLength(),
                            columnData.getLength() + 5, columnData.getPrecision(), false);
                }

                break;
            case "java.lang.Boolean":
                component = new JCheckBox();
                break;
            case "java.sql.Date":
                component = new ValidationDatePicker(columnData.isMandatory()?0:1, ALL);
//                component = new ValidationDateTextField(ALL, columnData.isMandatory());
                break;
        }

        return component;
    }

    private ComponentCreator() {
    }
}
