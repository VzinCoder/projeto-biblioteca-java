package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPostgres {
    private static final String URL = "jdbc:postgresql://localhost/biblioteca";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "12345678";


    public static Connection createConnection() throws ClassNotFoundException, SQLException{
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return conn;
    }
   

    public static void closeConnection(Connection conn) throws SQLException {
       conn.close();
    }


}
