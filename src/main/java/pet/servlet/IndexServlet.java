package pet.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class IndexServlet extends BaseServlet {
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
//                ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
//
//        IWebExchange webExchange = JavaxServletWebApplication.buildApplication(getServletContext())
//                .buildExchange(req, resp);
//
//        WebContext context = new WebContext(webExchange);
//
//        context.setVariable("name", "Huong Dan Java 1111111111111111111111111");
//
//        templateEngine.process("index", context, resp.getWriter());
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context.setVariable("user", req.getSession().getAttribute("user"));
        templateEngine.process("index", context, resp.getWriter());
    }
}
