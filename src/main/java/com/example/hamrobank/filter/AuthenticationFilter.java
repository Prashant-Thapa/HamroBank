package com.example.hamrobank.filter;

import com.example.hamrobank.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter for authentication and authorization
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/admin/*", "/customer/*"})
public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        
        // Check if user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        boolean isAdminResource = requestURI.contains("/admin/");
        boolean isCustomerResource = requestURI.contains("/customer/");
        
        if (!isLoggedIn) {
            // User is not logged in, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        } else {
            // User is logged in, check authorization
            User user = (User) session.getAttribute("user");
            
            if (isAdminResource && user.getRole() != User.Role.ADMIN) {
                // User is not an admin but trying to access admin resources
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/customer/dashboard");
                return;
            } else if (isCustomerResource && user.getRole() != User.Role.CUSTOMER && !isAdminResource) {
                // Admin is trying to access customer resources (but not through admin path)
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/dashboard");
                return;
            }
        }
        
        // User is authenticated and authorized, continue with the request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
