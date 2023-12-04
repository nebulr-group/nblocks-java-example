package com.mycompany.app;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessControlFilter implements Filter {
    private List<String> roles;
    private List<String> privileges;

    public AccessControlFilter(List<String> roles, List<String> privileges) {
        this.roles = roles;
        this.privileges = privileges;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Map<String, Object> user = (Map<String, Object>) httpRequest.getAttribute("user");
        if (user != null) {
            String role = (String) user.get("role");
            String scope = (String) user.get("scope");
            List<String> scopeList = List.of(scope.split(" "));
            boolean roleCheck = roles != null ? roles.contains(role) : false;
            boolean privilegeCheck = privileges != null ? scopeList.stream().anyMatch(privileges::contains) : false;
            if (roleCheck || privilegeCheck) {
                chain.doFilter(request, response);
            } else {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            }
        } else {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic here
    }

    @Override
    public void destroy() {
        // Cleanup logic here
    }
}