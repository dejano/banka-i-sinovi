package db;

import java.sql.SQLException;

public interface Storage<ID, T> {
    T store(T object);

    T retrieveById(ID key) throws SQLException;

    void delete(T object);

    void update(ID id, T object);
}
