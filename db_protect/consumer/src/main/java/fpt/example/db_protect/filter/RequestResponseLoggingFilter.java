package fpt.example.db_protect.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RequestResponseLoggingFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        System.out.println("  Logging Request  " + req.getMethod() + " : " + req.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("  Logging Response : " + res.getContentType());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
