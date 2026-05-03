package com.mobileshop.controller;

import com.mobileshop.model.User;
import com.mobileshop.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Admin User controller - manage users, approve registrations.
 */
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listUsers(request, response);
                break;
            case "pending":
                listPendingUsers(request, response);
                break;
            case "approve":
                approveUser(request, response);
                break;
            case "reject":
                rejectUser(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                listUsers(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            deleteUser(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    private void listPendingUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userService.getPendingUsers();
        request.setAttribute("users", users);
        request.setAttribute("isPendingList", true);
        request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
    }

    private void approveUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (userService.approveUser(id)) {
            request.getSession().setAttribute("success", "User approved successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to approve user.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users?action=pending");
    }

    private void rejectUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (userService.rejectUser(id)) {
            request.getSession().setAttribute("success", "User rejected.");
        } else {
            request.getSession().setAttribute("error", "Failed to reject user.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users?action=pending");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (userService.deleteUser(id)) {
            request.getSession().setAttribute("success", "User deleted successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to delete user.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
