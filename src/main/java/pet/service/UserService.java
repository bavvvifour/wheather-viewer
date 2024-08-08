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

    public boolean checkPassword(String inputPassword, String userPassword) {
        return passwordUtil.verifyHash(inputPassword, userPassword);
    }

    public User findByLogin(String login) {
        return userDao.findByLogin(login);
    }
}
