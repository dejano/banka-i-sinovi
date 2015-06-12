package util;

import rs.mgifos.mosquito.IMetaLoader;
import rs.mgifos.mosquito.LoadingException;
import rs.mgifos.mosquito.impl.pdm.PDMetaLoader;
import rs.mgifos.mosquito.model.MetaModel;
import rs.mgifos.mosquito.model.MetaTable;

import javax.inject.Singleton;
import java.util.Properties;

@Singleton
public class MosquitoFacadeImpl implements MosquitoFacade {

    private static final String PDM_FILE_LOCATION = "src/main/resources/meta/videoteka.pdm";
    private final MetaModel metaModel;

    public MosquitoFacadeImpl() throws LoadingException {
        IMetaLoader metaLoader = new PDMetaLoader();
        Properties properties = new Properties();
        properties.put(PDMetaLoader.FILENAME, PDM_FILE_LOCATION);
        metaModel = metaLoader.getMetaModel(properties);
    }

    public MetaTable loadMetaModel(String tableName) {
        return metaModel.getTableByCode(tableName);
    }

}
