package pet.dao;

import pet.exception.session.SessionAlreadyExistsException;
import pet.exception.session.SessionDeleteException;
import pet.exception.session.SessionNotFoundException;
import pet.exception.session.SessionsDeleteExpiredException;
import pet.model.Session;

public interface SessionDao {
    void saveSession(Session session) throws SessionAlreadyExistsException;
    Session findById(String sessionId) throws SessionNotFoundException;
    void deleteSession(String sessionId) throws SessionDeleteException;
    void deleteExpiredSessions() throws SessionsDeleteExpiredException;

}
