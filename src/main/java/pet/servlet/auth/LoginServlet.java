package pet.servlet.auth;

import pet.model.Session;
import pet.model.User;
import pet.service.SessionService;
import pet.service.UserService;
import pet.servlet.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private final UserService userService = new UserService();
    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        templateEngine.process("login", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            if (userService.authenticateUser(username, password)) {
                User user = userService.findByLogin(username);
                Session session = sessionService.createSession(user);

                Cookie cookie = new Cookie("sessionId", session.getId());
                cookie.setMaxAge(60 * 60);
                resp.addCookie(cookie);

                req.getSession().setAttribute("user", username);
                resp.sendRedirect("index");
            } else {
                context.setVariable("error", "Invalid username or password");
                templateEngine.process("login", context, resp.getWriter());
            }
        } catch (Exception e) {
            context.setVariable("error", e.getMessage());
            templateEngine.process("login", context, resp.getWriter());
        }
    }
}
