package gui.standard.form.misc;

import com.google.gson.annotations.SerializedName;
import meta.MosquitoSingletone;
import rs.mgifos.mosquito.model.MetaColumn;
import rs.mgifos.mosquito.model.MetaTable;

import java.util.HashMap;
import java.util.Map;

import static gui.standard.form.misc.FormData.ColumnGroupsEnum.BASE;
import static gui.standard.form.misc.FormData.ColumnGroupsEnum.PRIMARY_KEYS;
import static gui.standard.form.misc.ProcedureCallFactory.ProcedureCallEnum.*;

/**
 * Created by Nikola on 8.6.2015..
 */
public class ProcedureCallFactory {

    private static Map<String, Map<ProcedureCallEnum, String>> cachedParameterCalls = new HashMap<>();

    private ProcedureCallFactory() {
    }

    public static String getCreateProcedureCall(String tableName, int columnCount) {
        return getCreateProcedureCall(columnCount, 0, tableName, cachedParameterCalls.get(tableName));
    }

    public static String getProcedureCall(FormData formData, ProcedureCallEnum type) {
        String ret = null;

        int columnCount = formData.getCount(BASE);
        int pkColumnCount = formData.getCount(PRIMARY_KEYS);

        Map<ProcedureCallEnum, String> tableCpc = cachedParameterCalls.get(formData.getTableName());
        if (tableCpc == null) {
            tableCpc = new HashMap<>();
            cachedParameterCalls.put(formData.getTableName(), tableCpc);
        }

        switch (type) {
            case CREATE_PROCEDURE_CALL:
                ret = formData.getCustomProcedures().get(CREATE_PROCEDURE_CALL);
                if (ret == null)
                    ret = getCreateProcedureCall(columnCount, pkColumnCount,
                            formData.getTableName(), tableCpc);
                else
                    ret += addParams(columnCount);

                break;
            case UPDATE_PROCEDURE_CALL:
                ret = formData.getCustomProcedures().get(UPDATE_PROCEDURE_CALL);
                if (ret == null)
                    ret = getUpdateProcedureCall(columnCount, pkColumnCount,
                            formData.getTableName(), tableCpc);
                else
                    ret += addParams(pkColumnCount + columnCount);

                break;
            case DELETE_PROCEDURE_CALL:
                ret = formData.getCustomProcedures().get(DELETE_PROCEDURE_CALL);
                if (ret == null)
                    ret = getDeleteProcedureCall(columnCount, pkColumnCount,
                            formData.getTableName(), tableCpc);
                else
                    ret += addParams(pkColumnCount);

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

        sb.append(addParams(columnsCount));

        sb.append("}");

        return sb.toString();
    }

    private static String addParams(int columnCount) {
        StringBuilder sb = new StringBuilder();

        sb.append("( ");
        for (int i = 0; i < columnCount; i++) {
            sb.append("?, ");
        }
        removeLastComma(sb);
        sb.append(" )");

        return sb.toString();
    }

    private static void removeLastComma(StringBuilder sb) {
        int lastCommaIndex = sb.lastIndexOf(", ");
        if (lastCommaIndex != -1)
            sb.replace(lastCommaIndex, lastCommaIndex + 2, "");
    }

    public enum ProcedureCallEnum {
        @SerializedName("create")
        CREATE_PROCEDURE_CALL,

        @SerializedName("update")
        UPDATE_PROCEDURE_CALL,

        @SerializedName("delete")
        DELETE_PROCEDURE_CALL
    }

    private enum ProcedureCallStyle {
        USE_ALL_COLS, USE_PK_COLS, USE_PK_ALL_COLS
    }
}
