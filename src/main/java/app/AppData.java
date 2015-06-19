package app;

import gui.standard.ColumnValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 17.6.2015..
 */
public class AppData {

    private Map<String, String> values = new HashMap<>();

    private static AppData instance;

    public static AppData getInstance() {
        if (instance == null)
            instance = new AppData();

        return instance;
    }

    public void put(String key, String value) {
        values.put(key, value);
    }

    public String getValue(String key){
        return values.get(key);
    }

    public Map<String, String> getValues(Map<String, String> mapToAppData) {
        Map<String, String> ret = new HashMap<>();

        for (String key : mapToAppData.keySet()) {
            String value = values.get(key);
            System.out.println(mapToAppData.get(key) + "  " + value);
            ret.put(mapToAppData.get(key), value);
        }

        return ret;
    }

    private AppData() {

    }
}
