package core;

import com.google.gson.Gson;
import model.config.LookupConfig;
import model.config.MetaConfig;
import model.config.ZoomConfig;
import model.db.SchemaColumn;
import model.db.SchemaRow;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;
import util.MosquitoFacadeImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Singleton
public class ConfigData {

    private final static String CONFIG_FOLDER_PATH = "src/main/resources/meta/config/";
    private Map<String, MetaConfig> metaConfigMap;
    private Map<String, SchemaRow> schemaTableMap;
    private final Gson gson;
    private final MosquitoFacadeImpl mosquitoFacade;

    @Inject
    public ConfigData(MosquitoFacadeImpl mosquitoFacade) {
        metaConfigMap = new HashMap<>();
        schemaTableMap = new HashMap<>();
        gson = new Gson();
        this.mosquitoFacade = mosquitoFacade;
    }

    public void readConfigurations() {
        if (metaConfigMap.size() > 0) {
            System.out.println(metaConfigMap.size());
            return;
        }
        File folder = new File(CONFIG_FOLDER_PATH);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String[] name = file.getName().split("\\.(?=[^\\.]+$)");
                try {
                    readFile(name[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readFile(String fileName) throws IOException, ClassNotFoundException {
        BufferedReader br = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(CONFIG_FOLDER_PATH + fileName + ".json");
            br = new BufferedReader(fileReader);

            MetaConfig metaConfig = gson.fromJson(br, MetaConfig.class);
            metaConfigMap.put(metaConfig.getTableCode(), metaConfig);
            schemaTableMap.put(metaConfig.getTableCode(), adapt(metaConfig));
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public SchemaRow adapt(MetaConfig metaConfig) throws ClassNotFoundException {
        final MetaTable metaMetaTable = mosquitoFacade.loadMetaModel(metaConfig.getTableCode());

        @SuppressWarnings("unchecked")
        final Vector<MetaColumn> metaColumns = (Vector<MetaColumn>) metaMetaTable.cColumns();

        List<SchemaColumn> schemaColumnList = new ArrayList<SchemaColumn>();
        List<SchemaColumn> primaryKeys = new ArrayList<SchemaColumn>();
        List<String> lookupTableCodes = new ArrayList<String>();
        Map<SchemaColumn, List<SchemaColumn>> foreignKeys = new HashMap<SchemaColumn, List<SchemaColumn>>();
        List<ZoomConfig> zoomList = new ArrayList<>();
        zoomList.addAll(metaConfig.getZoomData());
        for (int i = 0; i < metaMetaTable.getTotalColumns(); i++) {
            SchemaColumn schemaColumn;
            MetaColumn metaColumn = metaColumns.get(i);
            boolean fk = metaColumn.isPartOfFK();
            boolean pk = metaColumn.isPartOfPK();
            schemaColumn = new SchemaColumn(metaColumn.getParentTable(), metaColumn.getName(), metaColumn.getCode(), Class.forName(metaColumn.getJClassName()), pk, fk);
            schemaColumnList.add(schemaColumn);
            if (fk) {
                for (LookupConfig lookupConfig : metaConfig.getLookups()) {
                    if (lookupConfig.getFrom().equals(metaColumn.getCode())) {

                        if (!lookupTableCodes.contains(lookupConfig.getTable())) {
                            lookupTableCodes.add(lookupConfig.getTable());
                        }
                        List<SchemaColumn> foreignColumnsList = new ArrayList<>();
                        for (LookupConfig.LookupColumn lookupColumn : lookupConfig.getColumns()) {
                            SchemaColumn refSchemaColumn = new SchemaColumn(lookupConfig.getTable(), lookupColumn.getLabel(), lookupColumn.getName(),Class.forName(metaColumn
                                    .getJClassName()), true, false);
                            schemaColumn.setReferencedTableName(lookupConfig.getTable());
                            schemaColumnList.add(refSchemaColumn);
                            foreignColumnsList.add(refSchemaColumn);
                        }
                        foreignKeys.put(schemaColumn, foreignColumnsList);
                    }
                }
            }

            if (pk) {
                primaryKeys.add(schemaColumn);
            }
        }

        return new SchemaRow(metaMetaTable.getCode(), metaMetaTable.getName(), schemaColumnList, primaryKeys, foreignKeys, lookupTableCodes, zoomList);
    }

    public Map<String, SchemaRow> getSchemaTableMap() {
        return Collections.unmodifiableMap(schemaTableMap);
    }

    public Map<String, MetaConfig> getMetaConfigMap() {
        return Collections.unmodifiableMap(metaConfigMap);
    }
}
