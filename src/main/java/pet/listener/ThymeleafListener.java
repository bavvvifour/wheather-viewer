package pet.listener;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafListener implements ServletContextListener {

    public static final String TEMPLATE_ENGINE_ATTR = "thymeleaf";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JavaxServletWebApplication application = JavaxServletWebApplication.buildApplication(sce.getServletContext());
        ITemplateEngine templateEngine = templateEngine(application);

        sce.getServletContext().setAttribute(TEMPLATE_ENGINE_ATTR, templateEngine);
    }

    private ITemplateEngine templateEngine(IWebApplication application) {
        TemplateEngine templateEngine = new TemplateEngine();

        WebApplicationTemplateResolver templateResolver = templateResolver(application);
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

    private WebApplicationTemplateResolver templateResolver(IWebApplication application) {
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // NOP
    }
}