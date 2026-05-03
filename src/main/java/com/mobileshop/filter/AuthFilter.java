package com.mobileshop.filter;

import com.mobileshop.model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Authentication filter for role-based access control.
 * Protects /admin/* and /user/* URLs.
 * Redirects unauthorized users to login page.
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        // Get session without creating a new one
        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            // Check for rememberMe cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("rememberMe".equals(cookie.getName())) {
                        // Redirect to auto-login or just let them login again
                        break;
                    }
                }
            }
            request.setAttribute("error", "Please log in to access this page.");
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");

        // Admin area protection - only admin role can access
        if (uri.startsWith(contextPath + "/admin/")) {
            if (!user.isAdmin()) {
                response.sendRedirect(contextPath + "/products");
                return;
            }
        }

        // User area protection - both admin and user can access
        if (uri.startsWith(contextPath + "/user/") || uri.startsWith(contextPath + "/orders") || uri.startsWith(contextPath + "/wishlist") || uri.startsWith(contextPath + "/profile")) {
            // Active users only
            if (!"active".equals(user.getStatus())) {
                session.invalidate();
                request.setAttribute("error", "Your account is not active. Please contact admin.");
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
