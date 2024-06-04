package fpt.example.db_protect.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//@Component
//@Order(1)
public class HeaderValidationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // Check if the required header is present and valid
        String headerValue = httpServletRequest.getHeader("X-Required-Header");
        if (headerValue == null || !headerValue.equals("FPT-INF")) {
            // If the header is missing or incorrect, send an error response and stop processing
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid X-Required-Header");
            return;
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
