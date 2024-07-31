package pet.service;

import pet.dao.UserDao;
import pet.dao.impl.UserDaoImpl;
import pet.model.User;
import pet.util.PasswordUtil;

public class UserService {
    private final UserDao userDao = new UserDaoImpl();

    private final PasswordUtil passwordUtil = new PasswordUtil();

    public void saveUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordUtil.hash(password));

        userDao.saveUser(user);
    }

    public boolean authenticateUser(String login, String password) {
        User user = userDao.findByLogin(login);
        return passwordUtil.verifyHash(password, user.getPassword());
    }

    public User findByLogin(String login) {
        return userDao.findByLogin(login);
    }

    public boolean existsByLogin(String login) {
        return userDao.existsByLogin(login);
    }
}
