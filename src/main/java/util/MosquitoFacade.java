package util;

import rs.mgifos.mosquito.model.MetaTable;

/**
 * Created by dejan on 6/9/2015.
 */
public interface MosquitoFacade {
    MetaTable loadMetaModel(String tableName);
}
