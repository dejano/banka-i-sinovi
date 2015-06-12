package db;

import controller.TableController;
import model.db.SchemaColumn;
import model.db.SchemaRow;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;
import javax.transaction.TransactionRolledbackException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionInterceptor implements MethodInterceptor {

    @Inject
    private DatabaseStorage databaseStorage;
    @Inject
    private DatabaseConnection databaseConnection;
    private MethodInvocation invocation;
    private TableController tableController;
    private SchemaRow schemaRow;
    private Map<String, String> values;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        this.invocation = invocation;
        String value = invocation.getMethod().getAnnotation(Transactional.class).value();

        for (Object o : invocation.getArguments()) {
            if (o.getClass().equals(TableController.class)) {
                tableController = (TableController) o;
            } else if (o.getClass().equals(SchemaRow.class)) {
                schemaRow = (SchemaRow) o;
            } else if (o.getClass().equals(HashMap.class)) {
                values = (HashMap<String, String>) o;
            }
        }

        if (schemaRow == null || values == null) {
            throw new InvalidParameterException("Invalid params passed in" + invocation.getMethod().getName());
        }

        switch (value) {
            case "modifyCheck": {
                beforeDelete();
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid annotation value.");
        }

        System.out.println("TransactionInterceptor.invoke - Everything looks good from here.");
        return invocation.proceed();
    }

    private void beforeDelete() throws Exception {
        try {
            databaseConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            List<String> primaryKeyValues = new ArrayList<>();
            for (SchemaColumn column : schemaRow.getPrimaryKeys()) {
                primaryKeyValues.add(values.get(column.getCode()));
            }
            List<String> rowFromDatabase = databaseStorage.selectById(schemaRow, primaryKeyValues);
            databaseConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            String[] rowFromTable = tableController.getSelectedRowData();

            if (rowFromDatabase.size() == 0) {
                throw new SQLException("Drugi korisnik je obrisao odabrani slog.", "", 50001);
            }

            for (int i = 0; i < rowFromDatabase.size(); i++) {
                if (!rowFromTable[i].equals(rowFromDatabase.get(i))) {
                    throw new Exception("Drugi korisnik je izvrsio izmene nad odabranim slogom. Nakon osvezavanja tabele pokusajte ponovo.");
//                    throw new SQLException("Drugi korisnik je izvrsio izmene nad odabranim slogom. Nakon osvezavanja tabele pokusajte ponovo.", "", 50000);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
