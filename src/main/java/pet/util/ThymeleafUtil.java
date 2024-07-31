package pet.util;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThymeleafUtil {
    public static WebContext buildWebContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
        JavaxServletWebApplication webApplication = JavaxServletWebApplication.buildApplication(servletContext);
        IServletWebExchange webExchange = webApplication.buildExchange(request, response);
        return new WebContext(webExchange);
    }
}
