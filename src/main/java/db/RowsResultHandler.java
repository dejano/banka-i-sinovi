package db;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RowsResultHandler implements ResultHandler<List<String[]>> {

    @Inject
    DatabaseConnection databaseConnection;

    public List<String[]> handle(Statement statement, ResultSet resultSet, Collection<String> columnCodes) throws SQLException {
        List<String[]> ret = new ArrayList<>();

        String[] rowValues = new String[columnCodes.size()];
        while (resultSet.next()) {
            int count = 0;
            for (String columnName : columnCodes) {
                rowValues[count++] = resultSet.getString(columnName);
            }

            ret.add(rowValues);
            rowValues = new String[columnCodes.size()];
        }

        resultSet.close();
        statement.close();

        databaseConnection.getConnection().commit();

        return ret;
    }
}
