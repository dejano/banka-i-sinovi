package meta.mainframe;

import gui.standard.ColumnValue;
import meta.JsonHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola on 11.6.2015..
 */
public class MainFrameMetaData {

    public static final String LOCATION = "src/main/resources/json/mainframe.json";

    private String title;
    private List<MetaMenu> menus = new ArrayList<>();

    public MainFrameMetaData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MetaMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<MetaMenu> menus) {
        this.menus = menus;
    }

    public static void main(String[] args) {
        MainFrameMetaData mfmt = new MainFrameMetaData();

        MetaMenu mm = new MetaMenu();
        mm.setText("Tabele");
        mfmt.getMenus().add(mm);

        MetaMenuItem mmi1 = new MetaMenuItem();
        mmi1.setText("Videoteka");
        mmi1.setFormName("videoteka");
        mm.getMenuItems().add(mmi1);

        MetaMenuItem mmi2 = new MetaMenuItem();
        mmi2.setText("Film");
        mmi2.setFormName("film");
        mm.getMenuItems().add(mmi2);

        MetaMenuItem mmi3 = new MetaMenuItem();
        mmi3.setText("Kopija");
        mmi3.setFormName("kopija");
        mm.getMenuItems().add(mmi3);

        JsonHelper.marshall(mfmt, LOCATION);
    }
}

