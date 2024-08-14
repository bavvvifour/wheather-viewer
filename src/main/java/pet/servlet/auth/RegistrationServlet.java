package pet.servlet.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import pet.model.User;
import pet.service.UserService;
import pet.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {

    private final UserService userService = new UserService();

    private Validator validator;

    @Override
    public void init() throws ServletException {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        templateEngine.process("registration", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("login");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        boolean hasErrors = false;

        User user = new User(username, password);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            String getPath = String.valueOf(violation.getPropertyPath());
            String result = getPath.substring(0, 1).toUpperCase() + getPath.substring(1);
            context.setVariable("error" + result, violation.getMessage());
            hasErrors = true;
        }

        if (username == null || username.isEmpty()) {
            context.setVariable("errorLogin", "Login is required.");
            hasErrors = true;
        }

        if (password == null || password.isEmpty()) {
            context.setVariable("errorPassword", "Password is required.");
            hasErrors = true;
        } else if (confirmPassword == null || confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
            context.setVariable("errorConfirmPassword", "Passwords do not match.");
            hasErrors = true;
        }

        assert password != null;
        if (password.equals(confirmPassword)) {
            userService.saveUser(username, password);
        } else {
            context.setVariable("errorLogin", "User already exists");
        }

        if (hasErrors) {
            context.setVariable("login", username);
            templateEngine.process("registration", context, resp.getWriter());
        } else {
            resp.sendRedirect(req.getContextPath() + "login");
        }
    }
}
