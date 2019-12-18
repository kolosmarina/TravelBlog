package by.training.kolos.controller;

import by.training.kolos.command.ApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * Класс для установки кодировки для запросов и ответов
 *
 * @author Колос Марина
 */
@WebFilter(urlPatterns = {"/*"},
        initParams = {@WebInitParam(name = "encoding", value = "UTF-8", description = "Encoding Param")})
public class EncodingFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    private String code;

    @Override
    public void init(FilterConfig filterConfig) {
        code = filterConfig.getInitParameter(ApplicationConstants.PARAM_ENCODING);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String codeRequest = servletRequest.getCharacterEncoding();
        if (code != null && !code.equalsIgnoreCase(codeRequest)) {
            servletRequest.setCharacterEncoding(code);
            servletResponse.setCharacterEncoding(code);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        code = null;
    }
}
