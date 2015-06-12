package db;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Singleton
public class DatabaseConnection {

    private static final String LOCK_TIMEOUT_INTERVAL = "5000";
    Map<String, String> config;
    private Connection conn;
    private String driver;
    private String url;
    private String username;
    private String password;

    @Inject
    public DatabaseConnection(@Named("driver") String driver,@Named("url") String url,@Named("username") String username,@Named("password") String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        System.out.println(password);
    }

    public Connection getConnection() {
        if (conn == null)
            try {
                open();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        return conn;
    }

    public void close() {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void open() throws ClassNotFoundException, SQLException {
        if (conn != null)
            return;

        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        conn.setAutoCommit(false);
        setLockTimeOut();
    }

    private void setLockTimeOut() throws SQLException {
        Statement stmt = conn.createStatement();
        String cmd = "SET LOCK_TIMEOUT " + LOCK_TIMEOUT_INTERVAL;
        stmt.execute(cmd);
    }

}
