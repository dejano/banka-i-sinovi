package meta.mainframe;

/**
 * Created by Nikola on 11.6.2015..
 */
public class MetaMenuItem {
    private String text;
    private String formName;

    public MetaMenuItem() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
