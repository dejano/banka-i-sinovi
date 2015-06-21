package app;

import com.google.gson.annotations.SerializedName;
import gui.standard.ColumnValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nikola on 17.6.2015..
 */
public class AppData {

    public enum AppDataEnum {
        @SerializedName("pibBanke")
        PIB_BANKE
    }

    private Map<AppDataEnum, String> values = new HashMap<>();


    private static AppData instance;

    public static AppData getInstance() {
        if (instance == null)
            instance = new AppData();

        return instance;
    }

    public void put(AppDataEnum key, String value) {
        values.put(key, value);
    }

    public String getValue(AppDataEnum key){
        return values.get(key);
    }

    public Map<String, String> getValues(Map<AppDataEnum, String> mapToAppData) {
        Map<String, String> ret = new HashMap<>();

        for (AppDataEnum key : mapToAppData.keySet()) {
            String value = values.get(key);
            System.out.println(mapToAppData.get(key) + "  " + value);
            ret.put(mapToAppData.get(key), value);
        }

        return ret;
    }

    private AppData() {

    }
}
