package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:sqlserver://ip:1433;"
                    + "databaseName=HelpDeskDB;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";

    private static final String USER = "name";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
