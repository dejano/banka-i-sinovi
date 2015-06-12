package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Set;

public interface ResultHandler<T> {

    T handle(Statement statement, ResultSet resultSet, Collection<String> columnCodes) throws SQLException;
}
