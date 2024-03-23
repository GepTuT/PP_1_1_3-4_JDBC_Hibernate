package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.transaction.TransactionalException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory;
    Transaction transaction = null;

    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users\n" +
                            "                (\n" +
                            "                    id        INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "                    name      VARCHAR(45) NOT NULL,\n" +
                            "                    lastName VARCHAR(45) NOT NULL,\n" +
                            "                    age       INT(3)     NOT NULL\n" +
                            "                 )")
                    .addEntity(User.class)
                    .executeUpdate();
            transaction.commit();

        } catch (TransactionalException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка в создании таблицы Users");
        }
    }

    @Override
    public void dropUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users")
                    .executeUpdate();
            transaction.commit();

        } catch (TransactionalException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при удалении таблицы Users");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (TransactionalException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка Update");
        }
        System.out.println(new StringBuilder("User с именем - ").append(name).append(" добавлен в базу данных"));
    }

    @Override
    public void removeUserById(long id) {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(session.get(User.class, id));
            transaction.commit();

        } catch (TransactionalException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка удаления по Id");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            users = session.createQuery("from User").getResultList();
            transaction.commit();
        } catch (TransactionalException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при получении списка Users");
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users").executeUpdate();
            transaction.commit();
        } catch (TransactionalException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при очищении таблицы Users");
        }
    }
}
