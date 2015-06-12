package factory;

import model.db.SchemaRow;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nikola on 8.6.2015..
 */
// TODO refactor
public class ProcedureCallFactory {

    private static Map<String, Map<ProcedureCallEnum, String>> cachedParameterCalls = new HashMap<>();

    private ProcedureCallFactory() {
    }

    public static String getProcedureCall(SchemaRow model, ProcedureCallEnum type) {
        String ret = null;

        int columnCount = model.getColumnCodesFromBaseTableOnly().size();
        int pkColumnCount = model.getPrimaryKeys().size();
        String tableCode = model.getTableCode();

        Map<ProcedureCallEnum, String> tableCpc = cachedParameterCalls.get(tableCode);
        if (tableCpc == null) {
            tableCpc = new HashMap<>();
            cachedParameterCalls.put(model.getTableCode(), tableCpc);
        }

        switch (type) {
            case CREATE_PROCEDURE_CALL:
                ret = getCreateProcedureCall(columnCount, pkColumnCount, tableCode, tableCpc);
                break;
            case UPDATE_PROCEDURE_CALL:
                ret = getUpdateProcedureCall(columnCount, pkColumnCount, tableCode, tableCpc);
                break;
            case DELETE_PROCEDURE_CALL:
                ret = getDeleteProcedureCall(columnCount, pkColumnCount, tableCode, tableCpc);
                break;
        }

        return ret;
    }

    private static String getCreateProcedureCall(int columnCount, int pkColumnCount,
                                                 String tableName, Map<ProcedureCallEnum, String> tableCpc) {
        String createProcedureCall = cachedParameterCalls.get(tableName)
                .get(ProcedureCallEnum.CREATE_PROCEDURE_CALL);

        // { call c_tableName(?,...,?)}

        if (createProcedureCall == null) {
            createProcedureCall = getProcedureCall(columnCount, pkColumnCount, tableName, "c",
                    ProcedureCallStyle.USE_ALL_COLS);

            tableCpc.put(ProcedureCallEnum.CREATE_PROCEDURE_CALL, createProcedureCall);
        }

        return createProcedureCall;
    }

    private static String getUpdateProcedureCall(int columnCount, int pkColumnCount,
                                                 String tableName, Map<ProcedureCallEnum, String> tableCpc) {
        String updateProcedureCall = tableCpc.get(ProcedureCallEnum.UPDATE_PROCEDURE_CALL);

        // { call u_tableName(?,...,?)}

        if (updateProcedureCall == null) {
            updateProcedureCall = getProcedureCall(columnCount, pkColumnCount, tableName, "u",
                    ProcedureCallStyle.USE_PK_ALL_COLS);

            tableCpc.put(ProcedureCallEnum.UPDATE_PROCEDURE_CALL, updateProcedureCall);
        }

        return updateProcedureCall;
    }

    private static String getDeleteProcedureCall(int columnCount, int pkColumnCount,
                                                 String tableName, Map<ProcedureCallEnum, String> tableCpc) {
        String deleteProcedureCall = tableCpc.get(ProcedureCallEnum.DELETE_PROCEDURE_CALL);

        if (deleteProcedureCall == null) {
            deleteProcedureCall = getProcedureCall(columnCount, pkColumnCount, tableName, "d",
                    ProcedureCallStyle.USE_PK_COLS);

            tableCpc.put(ProcedureCallEnum.DELETE_PROCEDURE_CALL, deleteProcedureCall);
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
