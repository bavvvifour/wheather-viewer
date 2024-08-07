package pet.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.dao.UserDao;
import pet.exception.UnexpectedException;
import pet.exception.user.UserAlreadyExistsException;
import pet.exception.user.UserNotFoundException;
import pet.model.User;
import pet.util.PersistenceUtil;


public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void saveUser(User user) throws UserAlreadyExistsException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
            logger.info("User {} saved successfully", user.getLogin());
        } catch (Exception e) {
            if (transaction != null) {
                logger.error("User {} already exists", user.getLogin());
                transaction.rollback();
            }
            throw new UserAlreadyExistsException("User " + user.getLogin() + " already exists", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User findByLogin(String login) throws UserNotFoundException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        User user = null;
        try {
            user = entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
            logger.info("User found by login: {}", login);
        } catch (Exception e) {
            logger.warn("User with login {} not found", login);
            throw new UserNotFoundException("User " + login + " not found", e);
        } finally {
            entityManager.close();
        }
        return user;
    }

    @Override
    public boolean existsByLogin(String login) throws UnexpectedException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        boolean exists = false;
        try {
            long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.login = :login", Long.class)
                    .setParameter("login", login)
                    .getSingleResult();
            exists = count > 0;
        } catch (Exception e) {
            logger.warn("Unexpected exception while checking if user exists by login: {}", login, e);
            throw new UnexpectedException("Unexpected exception", e);
        }
        finally {
            entityManager.close();
        }
        return exists;
    }
}
