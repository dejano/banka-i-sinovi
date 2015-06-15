package meta;

import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import java.util.*;

/**
 * Created by Nikola on 15.6.2015..
 */
public class SuperMetaTable {
    private MetaTable metaTable;

    public SuperMetaTable(MetaTable metaTable) {
        this.metaTable = metaTable;
    }

    public Map<String, String> getBaseColumnTypes() {
        Map<String, String> ret = new HashMap<>();

        Iterator it = metaTable.cColumns().iterator();
        while (it.hasNext()) {
            MetaColumn column = (MetaColumn) it.next();

            ret.put(column.getCode(), column.getJClassName());
        }

        return ret;
    }

    public List<String> getBaseColumnCodes() {
        List<String> ret = new ArrayList<>();

        Iterator it = metaTable.cColumns().iterator();
        while (it.hasNext()) {
            MetaColumn column = (MetaColumn) it.next();

            ret.add(column.getCode());
        }

        return ret;
    }

    public List<String> getPkColumnCodes() {
        List<String> ret = new ArrayList<>();

        Iterator it = metaTable.cColumns().iterator();
        while (it.hasNext()) {
            MetaColumn column = (MetaColumn) it.next();

            if (column.isPartOfPK())
                ret.add(column.getCode());
        }

        return ret;
    }

    public Map<String, String> getPkColumnTypes() {
        Map<String, String> ret = new HashMap<>();

        Iterator it = metaTable.cColumns().iterator();
        while (it.hasNext()) {
            MetaColumn column = (MetaColumn) it.next();

            if (column.isPartOfPK())
                ret.put(column.getCode(), column.getJClassName());
        }

        return ret;
    }
}
