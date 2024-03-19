package jm.task.core.jdbc.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final String URL = "jdbc:mysql://localhost:4321/mydbtest";
    private static final String USERNAME = "mysql";
    private static final String PASSWORD = "mysql";


    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Нет соединения с БД");
        }
        return null;
    }

}
