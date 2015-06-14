package meta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gui.standard.ColumnMapping;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    public static void marshall(Object object, String filename) {
        try {
            File jsonFile = new File(filename);
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String json = gson.toJson(object);

            FileWriter writer = new FileWriter(jsonFile);
            writer.write(json);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T unmarshall(String filename, Class<T> clazz) {
        T ret = null;

        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(new File(filename)));

            ret = gson.fromJson(br, clazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return ret;
    }

}
