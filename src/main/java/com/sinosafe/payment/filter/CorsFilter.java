package com.sinosafe.payment.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with base.
 * User: anguszhu
 * Date: Dec,10 2015
 * Time: 12:48 PM
 * description:
 */
@Component
public class CorsFilter implements Filter{

    // For security reasons set this regex to an appropriate value
    // example: ".*example\\.com"
    private static final String ALLOWED_DOMAINS_REGEXP = "*";

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        //String origin = req.getHeader("Origin");
        String origin = ALLOWED_DOMAINS_REGEXP;

       // if (true || origin != null && origin.matches(ALLOWED_DOMAINS_REGEXP)) {
            resp.addHeader("Access-Control-Allow-Origin", origin);
            resp.addHeader("Access-Control-Expose-Headers", "token");

            if ("options".equalsIgnoreCase(req.getMethod())) {

                resp.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
                if (origin != null) {
                    String headers = req.getHeader("Access-Control-Request-Headers");
                    String method = req.getHeader("Access-Control-Request-Method");
                    resp.addHeader("Access-Control-Allow-Methods", method);
                    resp.addHeader("Access-Control-Allow-Headers", headers);
                    resp.setContentType("text/plain");
                }
                resp.getWriter().flush();
                return;
            }
      //  }

        // Fix ios6 caching post requests
        if ("post".equalsIgnoreCase(req.getMethod())) {
            resp.addHeader("Cache-Control", "no-cache");
        }

        if (filterChain != null) {
            filterChain.doFilter(req, resp);
        }
    }

    @Override public void destroy() {}

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }
}