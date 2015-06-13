package meta;

import rs.mgifos.mosquito.IMetaLoader;
import rs.mgifos.mosquito.LoadingException;
import rs.mgifos.mosquito.impl.pdm.PDMetaLoader;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaModel;
import rs.mgifos.mosquito.model.MetaTable;

import java.util.Properties;
import java.util.Vector;

public class MosquitoSingletone {

    private static final String PDM_FILE_LOCATION = "src/main/resources/";
    //    private static final String FILE_NAME = "videoteka.pdm";
    private static final String FILE_NAME = "banka.pdm";

    private static MosquitoSingletone instance;

    private MetaModel metaModel;

    private MosquitoSingletone() {
        IMetaLoader metaLoader = new PDMetaLoader();
        Properties properties = new Properties();
        properties.put(PDMetaLoader.FILENAME, PDM_FILE_LOCATION + FILE_NAME);

        try {
            this.metaModel = metaLoader.getMetaModel(properties);
        } catch (LoadingException e) {
            e.printStackTrace();
        }
    }

    public static MosquitoSingletone getInstance() {
        if (instance == null)
            instance = new MosquitoSingletone();

        return instance;
    }

    public MetaTable getMetaTable(String tableName) {
        return this.metaModel.getTableByCode(tableName);
    }

    public static void main(String[] args) throws LoadingException {
        IMetaLoader metaLoader = new PDMetaLoader();
        Properties properties = new Properties();
        properties.put(PDMetaLoader.FILENAME,
                PDM_FILE_LOCATION + FILE_NAME);
        MetaModel model = metaLoader.getMetaModel(properties);

        for (MetaTable table : model) {
            System.out.println(table.getCode());

            System.out.println(table.getColByTableDotColumnCode(table.getCode() + ".SIFRA_VIDEOTEKE"));

            @SuppressWarnings("unchecked")
            Vector<MetaColumn> columns = (Vector<MetaColumn>) table.cColumns();
            for (int i = 0; i < table.getTotalColumns(); i++) {
                MetaColumn column = columns.get(i);
                System.out.println("\t" + column.getName() + " " + "");
            }
        }
    }
}
