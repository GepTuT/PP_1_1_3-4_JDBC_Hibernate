package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
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
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении таблицы Users");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO users (name, last_name, age) VALUES (?,?,?)")) {
                statement.setString(1, name);
                statement.setString(2, lastName);
                statement.setByte(3, age);
                statement.executeUpdate();
            } catch (SQLException exception) {
                connection.rollback();
                System.out.println("Ошибка Update");
            }
            connection.commit();
        } catch (SQLException exception) {
            System.out.println("Ошибка при добавлении User");
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("DELETE FROM users WHERE id = ?")) {
                statement.setLong(1, id);
                statement.executeUpdate();
            } catch (SQLException exception) {
                connection.rollback();
                System.out.println("Ошибка Update");
            }
            connection.commit();
        } catch (SQLException exception) {
            System.out.println("Ошибка при удалении User по ID");
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT id, name, last_name, age FROM users")) {
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
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка Users");
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении таблицы Users");
        }
    }
}
