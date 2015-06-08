package gui.standard.form.misc;

import meta.MosquitoSingletone;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import java.util.HashMap;
import java.util.Map;

import static gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum.*;

/**
 * Created by Nikola on 8.6.2015..
 */
// TODO refactor
public class ProcedureCallFactory {

    private static Map<String, Map<ProcedureCallEnum, String>> cachedParameterCalls = new HashMap<>();

    private ProcedureCallFactory() {
    }

    public static String getProcedureCall(String tableName, ProcedureCallEnum type) {
        String ret = null;

        MetaTable table = MosquitoSingletone.getInstance().getMetaTable(tableName);

        int columnCount = table.cColumns().size();
        int pkColumnCount = 0;

        for (Object col : table.cColumns()) {
            if (((MetaColumn) col).isPartOfPK())
                pkColumnCount++;
        }

        Map<ProcedureCallEnum, String> tableCpc = cachedParameterCalls.get(tableName);
        if (tableCpc == null) {
            tableCpc = new HashMap<>();
            cachedParameterCalls.put(tableName, tableCpc);
        }

        switch (type) {
            case CREATE_PROCEDURE_CALL:
                ret = getCreateProcedureCall(columnCount, pkColumnCount, tableName, tableCpc);
                break;
            case UPDATE_PROCEDURE_CALL:
                ret = getUpdateProcedureCall(columnCount, pkColumnCount, tableName, tableCpc);
                break;
            case DELETE_PROCEDURE_CALL:
                ret = getDeleteProcedureCall(columnCount, pkColumnCount, tableName, tableCpc);
                break;
        }

        return ret;
    }

    private static String getCreateProcedureCall(int columnCount, int pkColumnCount,
                                                 String tableName, Map<ProcedureCallEnum, String> tableCpc) {
        String createProcedureCall = cachedParameterCalls.get(tableName)
                .get(CREATE_PROCEDURE_CALL);

        // { call c_tableName(?,...,?)}

        if (createProcedureCall == null) {
            createProcedureCall = getProcedureCall(columnCount, pkColumnCount, tableName, "c",
                    ProcedureCallStyle.USE_ALL_COLS);

            tableCpc.put(CREATE_PROCEDURE_CALL, createProcedureCall);
        }

        return createProcedureCall;
    }

    private static String getUpdateProcedureCall(int columnCount, int pkColumnCount,
                                                 String tableName, Map<ProcedureCallEnum, String> tableCpc) {
        String updateProcedureCall = tableCpc.get(UPDATE_PROCEDURE_CALL);

        // { call u_tableName(?,...,?)}

        if (updateProcedureCall == null) {
            updateProcedureCall = getProcedureCall(columnCount, pkColumnCount, tableName, "u",
                    ProcedureCallStyle.USE_PK_ALL_COLS);

            tableCpc.put(UPDATE_PROCEDURE_CALL, updateProcedureCall);
        }

        return updateProcedureCall;
    }

    private static String getDeleteProcedureCall(int columnCount, int pkColumnCount,
                                                 String tableName, Map<ProcedureCallEnum, String> tableCpc) {
        String deleteProcedureCall = tableCpc.get(DELETE_PROCEDURE_CALL);

        if (deleteProcedureCall == null) {
            deleteProcedureCall = getProcedureCall(columnCount, pkColumnCount, tableName, "d",
                    ProcedureCallStyle.USE_PK_COLS);

            tableCpc.put(DELETE_PROCEDURE_CALL, deleteProcedureCall);
        }

        return deleteProcedureCall;
    }


    private static String getProcedureCall(int columnCount, int pkColumnCount,
                                           String tableName, String prefix, ProcedureCallStyle pcs) {
        StringBuilder sb = new StringBuilder();

        int columnsCount = 0;

        switch (pcs) {
            case USE_ALL_COLS:
                columnsCount = columnCount;
                break;
            case USE_PK_COLS:
                columnsCount = pkColumnCount;
                break;
            case USE_PK_ALL_COLS:
                columnsCount = columnCount + pkColumnCount;
                break;
        }

        sb.append("{ call ");
        sb.append(prefix);
        sb.append("_");
        sb.append(tableName);
        sb.append("( ");

        for (int i = 0; i < columnsCount; i++) {
            sb.append("?, ");
        }

        int lastCommaIndex = sb.lastIndexOf(", ");
        sb.delete(lastCommaIndex, lastCommaIndex + 2);
        sb.append(" )}");

        return sb.toString();
    }

    public enum ProcedureCallEnum {
        CREATE_PROCEDURE_CALL,
        UPDATE_PROCEDURE_CALL,
        DELETE_PROCEDURE_CALL
    }

    private enum ProcedureCallStyle {
        USE_ALL_COLS, USE_PK_COLS, USE_PK_ALL_COLS
    }
}
