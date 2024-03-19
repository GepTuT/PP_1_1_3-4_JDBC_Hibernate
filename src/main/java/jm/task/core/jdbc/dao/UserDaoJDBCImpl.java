package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection;


    public UserDaoJDBCImpl() {
        connection = Util.getConnection();
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users\n" +
                    "                (\n" +
                    "                    id        INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "                    name      VARCHAR(45) NOT NULL,\n" +
                    "                    last_name VARCHAR(45) NOT NULL,\n" +
                    "                    age       INT(3)     NOT NULL\n" +
                    "                 )");

        } catch (SQLException e) {
            System.out.println("Ошибка в создании таблицы Users");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении таблицы Users");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = connection
                .prepareStatement("INSERT INTO users (name, last_name, age) VALUES (?,?,?)")) {
            connection.setAutoCommit(false);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка Update");
        }
        System.out.println(new StringBuilder("User с именем - ").append(name).append(" добавлен в базу данных"));
    }

    public void removeUserById(long id) {

        try (PreparedStatement statement = connection
                .prepareStatement("DELETE FROM users WHERE id = ?")) {
            connection.setAutoCommit(false);
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException exception) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка удаления по Id");
        }

    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, name, last_name, age FROM users")) {
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("last_name"),
                        resultSet.getByte("age")
                );
                user.setId(resultSet.getLong("id"));
                userList.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Ошибка при получении списка Users");
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении таблицы Users");
        }
    }
}
