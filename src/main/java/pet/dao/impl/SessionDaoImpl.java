package pet.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pet.dao.SessionDao;
import pet.exception.session.SessionsDeleteExpiredException;
import pet.exception.session.SessionAlreadyExistsException;
import pet.exception.session.SessionDeleteException;
import pet.exception.session.SessionNotFoundException;
import pet.model.Session;
import pet.util.PersistenceUtil;

import java.time.LocalDateTime;
import java.util.List;

public class SessionDaoImpl implements SessionDao {
    private static final Logger logger = LoggerFactory.getLogger(SessionDaoImpl.class);

    @Override
    public void saveSession(Session session) throws SessionAlreadyExistsException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(session);
            transaction.commit();
            logger.info("Session {} saved successfully", session.getId());
        } catch (Exception e) {
            if (transaction != null) {
                logger.error("Failed to save session {}, session already exists", session.getId(), e);
                transaction.rollback();
            }
            throw new SessionAlreadyExistsException("Session " + session.getId() + " already exists", e);
        } finally {
            entityManager.close();
        }

    }

    @Override
    public Session findById(String sessionId) throws SessionNotFoundException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        Session session = null;
        try {
            session = entityManager.find(Session.class, sessionId);
            if (session != null) {
                logger.info("Session {} found", sessionId);
            } else {
                logger.warn("Session {} not found", sessionId);
                throw new SessionNotFoundException("Session " + sessionId + " not found");
            }
        } catch (Exception e) {
            logger.error("Session {} not found", sessionId);
            throw new SessionNotFoundException("Session " + sessionId + " not found", e);
        } finally {
            entityManager.close();
        }
        return session;
    }

    @Override
    public void deleteSession(String sessionId) throws SessionDeleteException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Session session = entityManager.find(Session.class, sessionId);
            if (session != null) {
                entityManager.remove(session);
                transaction.commit();
                logger.info("Session {} deleted successfully", sessionId);
            } else {
                logger.warn("Session {} not found, nothing to delete", sessionId);
                transaction.rollback();
                throw new SessionDeleteException("Session " + sessionId + " not found, nothing to delete");
            }
        } catch (Exception e) {
            if (transaction != null) {
                logger.error("Failed to delete session {}", sessionId, e);
                transaction.rollback();
            }
            throw new SessionDeleteException("Failed to delete session " + sessionId, e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void deleteExpiredSessions() throws SessionsDeleteExpiredException {
        EntityManager entityManager = PersistenceUtil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            List<Session> expiredSessions = entityManager.createQuery("SELECT s FROM Session s WHERE s.expiresAt < :now", Session.class)
                    .setParameter("now", LocalDateTime.now())
                    .getResultList();

            for (Session session : expiredSessions) {
                entityManager.remove(session);
                logger.info("Deleted expired session with ID: {}", session.getId());
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                logger.error("Error deleting expired sessions", e);
                transaction.rollback();
            }
            throw new SessionsDeleteExpiredException("Error deleting expired sessions", e);
        } finally {
            entityManager.close();
        }
    }
}
