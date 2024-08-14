package pet.security;

import pet.model.Session;
import pet.service.SessionService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private final SessionService sessionService = new SessionService();

    private static final List<String> ALLOWED_PATHS = Arrays.asList("/login", "/registration");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "");
        boolean isAllowedPath = ALLOWED_PATHS.contains(path);
        if (isAllowedPath) {
            chain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = req.getCookies();
        String sessionId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    req.getSession().setAttribute("user", sessionService.findSessionById(sessionId).getUser().getLogin());
                    break;
                }
            }
        }

        if (sessionId != null) {
            Session session = sessionService.findSessionById(sessionId);
            if (session != null && session.getExpiresAt().isAfter(LocalDateTime.now())) {
                chain.doFilter(request, response);
                return;
            }
        }

        resp.sendRedirect(req.getContextPath() + "/login");
    }

    @Override
    public void destroy() {
//        Filter.super.destroy();
    }
}
