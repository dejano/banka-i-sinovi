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

    public static void main(String[] args) {
        FormMetaData fmd1 = new FormMetaData();
        fmd1.setTableName("VIDEOTEKA");
        fmd1.setTitle("Videoteka");

        NextMetaData nmd1 = new NextMetaData();
        nmd1.setFormName("film");
        nmd1.getColumnCodeMapping().add(new ColumnMapping("SIFRA_VIDEOTEKE", "SIFRA_VIDEOTEKE"));
        fmd1.getNextData().add(nmd1);

        marshall(fmd1, "json/forms/videoteka.json");

        FormMetaData fmd2 = new FormMetaData();
        fmd2.setTableName("FILM");
        fmd2.setTitle("Film");

        NextMetaData nmd2 = new NextMetaData();
        nmd2.setFormName("kopija");
        nmd2.getColumnCodeMapping().add(new ColumnMapping("SIFRA_FILMA", "SIFRA_FILMA"));
        fmd2.getNextData().add(nmd2);

        NextMetaData nmd22 = new NextMetaData();
        nmd22.setFormName("kopija");
        nmd22.getColumnCodeMapping().add(new ColumnMapping("SIFRA_FILMA", "SIFRA_FILMA"));
        fmd2.getNextData().add(nmd22);
        List<String> lookups = new ArrayList<>();
        lookups.add("NAZIV");
        Lookup l1 = new Lookup("VIDEOTEKA", "SIFRA_VIDEOTEKE", "SIFRA_VIDEOTEKE", lookups);
        fmd2.putLookup("SIFRA_VIDEOTEKE", l1);

        marshall(fmd2, "json");
    }
}
