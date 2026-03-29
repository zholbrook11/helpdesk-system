package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:sqlserver://54.226.8.17:1433;"
                    + "databaseName=HelpDeskDB;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";

    private static final String USER = "Quentin";
    private static final String PASSWORD = "CSI3370nP3h";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
