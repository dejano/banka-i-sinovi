package gui.standard.form;

import javax.swing.*;

public class StatusBar extends JPanel {

    private static final long serialVersionUID = 1L;
    private FormModeEnum mode;
    private JLabel statusText;

    public StatusBar() {
        statusText = new JLabel();
        this.add(statusText);
    }

    public FormModeEnum getMode() {
        return mode;
    }

    public void setMode(FormModeEnum mode) {
        String text = null;

        this.mode = mode;
        System.out.println(mode);
        switch (mode) {
            case ADD:
                text = "Dodavanje novog sloga";
                break;
            case EDIT:
                text = "Izmena sloga";
                break;
            case SEARCH:
                text = "Pretraga";
                break;
            case DEFAULT:
                text = " ";
                break;
        }

        statusText.setText(text);
    }

    public enum FormModeEnum {
        DEFAULT, ADD, EDIT, SEARCH
    }

}
