package by.training.kolos.controller;

import by.training.kolos.command.ApplicationConstants;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/index.jsp"},
        initParams = {@WebInitParam(name = "locale", value = "ru_RU", description = "Locale Param")})
public class LocaleFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();

    private String locale;

    @Override
    public void init(FilterConfig filterConfig) {
        locale = filterConfig.getInitParameter(ApplicationConstants.PARAM_LOCALE);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpSession session = req.getSession();
        String sessionLocale = (String) session.getAttribute(ApplicationConstants.PARAM_LOCALE);
        if (sessionLocale == null) {
            session.setAttribute(ApplicationConstants.PARAM_LOCALE, locale);
            logger.log(Level.DEBUG, "Starting the application, session locale set: {}",
                    (String) session.getAttribute(ApplicationConstants.PARAM_LOCALE));
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void destroy() {
        locale = null;
    }
}
