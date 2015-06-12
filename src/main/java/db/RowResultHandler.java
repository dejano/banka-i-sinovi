package db;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class RowResultHandler implements ResultHandler<List<String>> {

    @Inject
    DatabaseConnection databaseConnection;

    @Override
    public List<String> handle(Statement statement, ResultSet resultSet, Collection<String> columnCodes) throws SQLException {
        List<String> rowValues = new ArrayList<>();
        while (resultSet.next()) {
            for (String columnName : columnCodes) {
                rowValues.add(resultSet.getString(columnName));
            }
        }

        resultSet.close();
        statement.close();

        databaseConnection.getConnection().commit();

        return rowValues;
    }

}
