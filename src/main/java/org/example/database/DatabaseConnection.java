package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static String url = "jdbc:mysql://localhost:3307/gestionpasseport";
    private static String user = "root";
    private static String password = "";
    private static Connection connection;
    public static Connection connectToDB() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return connection;
    }
}
