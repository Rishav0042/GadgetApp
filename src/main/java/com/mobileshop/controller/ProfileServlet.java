package com.mobileshop.controller;

import com.mobileshop.model.User;
import com.mobileshop.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Profile controller - view and update user profile, change password.
 */
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "view";

        switch (action) {
            case "view":
                viewProfile(request, response);
                break;
            case "edit":
                editProfileForm(request, response);
                break;
            case "changePassword":
                request.getRequestDispatcher("/user/change-password.jsp").forward(request, response);
                break;
            default:
                viewProfile(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        if ("update".equals(action)) {
            updateProfile(request, response);
        } else if ("changePassword".equals(action)) {
            changePassword(request, response);
        } else {
            viewProfile(request, response);
        }
    }

    private void viewProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        request.setAttribute("profileUser", user);
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    private void editProfileForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        request.setAttribute("profileUser", user);
        request.getRequestDispatcher("/user/edit-profile.jsp").forward(request, response);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        user.setEmail(request.getParameter("email"));
        user.setFullName(request.getParameter("fullName"));
        user.setPhone(request.getParameter("phone"));
        user.setAddress(request.getParameter("address"));

        if (userService.updateUser(user)) {
            // Update session
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("user", user);
            request.getSession().setAttribute("success", "Profile updated successfully!");
            response.sendRedirect(request.getContextPath() + "/profile?action=view");
        } else {
            request.setAttribute("error", "Failed to update profile. Email or phone may already be in use.");
            request.setAttribute("profileUser", user);
            request.getRequestDispatcher("/user/edit-profile.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New passwords do not match.");
            request.getRequestDispatcher("/user/change-password.jsp").forward(request, response);
            return;
        }

        StringBuilder errorMsg = new StringBuilder();
        if (userService.updatePassword(userId, currentPassword, newPassword, errorMsg)) {
            request.getSession().setAttribute("success", "Password changed successfully!");
            response.sendRedirect(request.getContextPath() + "/profile?action=view");
        } else {
            request.setAttribute("error", errorMsg.toString());
            request.getRequestDispatcher("/user/change-password.jsp").forward(request, response);
        }
    }
}
