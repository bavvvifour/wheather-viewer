package pet.servlet;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import pet.exception.UnexpectedException;
import pet.exception.session.SessionAlreadyExistsException;
import pet.exception.session.SessionDeleteException;
import pet.exception.session.SessionNotFoundException;
import pet.exception.session.SessionsDeleteExpiredException;
import pet.exception.user.UserAlreadyExistsException;
import pet.exception.user.UserNotFoundException;
import pet.listener.ThymeleafListener;
import pet.util.ThymeleafUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;

public abstract class BaseServlet extends HttpServlet {

    protected ITemplateEngine templateEngine;
    protected WebContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        templateEngine = (ITemplateEngine) config.getServletContext().getAttribute(
                ThymeleafListener.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        context = ThymeleafUtil.buildWebContext(req, res, getServletContext());

        try {
            super.service(req, res);
        } catch (Exception e) {
            handleException(e, req, res);
        }
    }

    protected void handleException(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (
                e instanceof InvalidParameterException ||

                e instanceof UserAlreadyExistsException ||
                e instanceof UserNotFoundException ||

                e instanceof SessionAlreadyExistsException ||
                e instanceof SessionDeleteException ||
                e instanceof SessionNotFoundException ||
                e instanceof SessionsDeleteExpiredException ||

                e instanceof UnexpectedException
        ) {
            context.setVariable("error", e.getMessage());
            templateEngine.process("error", context, resp.getWriter());
        } else {
            context.setVariable("error", "An unexpected error occurred.");
            templateEngine.process("error", context, resp.getWriter());
        }
    }
}
