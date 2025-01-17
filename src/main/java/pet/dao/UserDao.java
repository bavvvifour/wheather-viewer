package pet.dao;

import pet.exception.user.UserAlreadyExistsException;
import pet.exception.user.UserNotFoundException;
import pet.model.User;

public interface UserDao {
    void saveUser(User user) throws UserAlreadyExistsException;
    User findByLogin(String login) throws UserNotFoundException;
}
