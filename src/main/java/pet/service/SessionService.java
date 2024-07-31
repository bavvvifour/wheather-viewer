package pet.service;

import pet.dao.SessionDao;
import pet.dao.impl.SessionDaoImpl;
import pet.model.Session;
import pet.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionService {
    private final SessionDao sessionDao = new SessionDaoImpl();

    public Session createSession(User user) {
        Session session = new Session();
        session.setId(UUID.randomUUID().toString());
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plusHours(1));

        sessionDao.saveSession(session);
        return session;
    }

    public Session findSessionById(String sessionId) {
        return sessionDao.findById(sessionId);
    }

    public void deleteSession(String sessionId) {
        sessionDao.deleteSession(sessionId);
    }

    public void deleteExpiredSessions() {
        sessionDao.deleteExpiredSessions();
    }
}
