package meta.mainframe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 11.6.2015..
 */
public class MetaMenu {
    private String text;
    private List<MetaMenuItem> menuItems = new ArrayList<>();

    public MetaMenu() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<MetaMenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MetaMenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
