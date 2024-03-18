package jm.task.core.jdbc.util;

import java.sql.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final String URL = "jdbc:mysql://localhost:4321/mydbtest";
    private static final String USERNAME = "mysql";
    private static final String PASSWORD = "mysql";


    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("Нет соединения с БД");
        }
        return connection;
    }

}
