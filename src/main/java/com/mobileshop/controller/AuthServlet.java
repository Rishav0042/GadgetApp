package com.mobileshop.controller;

import com.mobileshop.model.User;
import com.mobileshop.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Authentication controller handling login, register, and logout.
 */
public class AuthServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "login";

        switch (action) {
            case "register":
                request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
                break;
            case "login":
                request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
                break;
            case "logout":
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                // Clear cookies
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("rememberMe".equals(cookie.getName())) {
                            cookie.setMaxAge(0);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    }
                }
                response.sendRedirect(request.getContextPath() + "/auth?action=login");
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/auth?action=login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/auth?action=login");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");

        StringBuilder errorMsg = new StringBuilder();
        User user = userService.authenticateUser(username, password, errorMsg);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userName", user.getFullName());

            // Remember me cookie
            if ("on".equals(remember)) {
                Cookie rememberCookie = new Cookie("rememberMe", user.getUsername());
                rememberCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
                rememberCookie.setHttpOnly(true);
                rememberCookie.setPath("/");
                response.addCookie(rememberCookie);
            }

            // Redirect based on role
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/products");
            }
        } else {
            request.setAttribute("error", errorMsg.toString());
            request.setAttribute("username", username);
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Check password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            preserveRegisterFields(request, username, email, fullName, phone, address);
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
            return;
        }

        StringBuilder errorMsg = new StringBuilder();
        User user = userService.registerUser(username, email, password, fullName, phone, address, errorMsg);

        if (user != null) {
            request.setAttribute("success", "Registration successful! Your account is pending admin approval. Please wait for approval before logging in.");
            request.getRequestDispatcher("/auth/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", errorMsg.toString());
            preserveRegisterFields(request, username, email, fullName, phone, address);
            request.getRequestDispatcher("/auth/register.jsp").forward(request, response);
        }
    }

    private void preserveRegisterFields(HttpServletRequest request, String username, String email, String fullName, String phone, String address) {
        request.setAttribute("regUsername", username);
        request.setAttribute("regEmail", email);
        request.setAttribute("regFullName", fullName);
        request.setAttribute("regPhone", phone);
        request.setAttribute("regAddress", address);
    }
}
