package com.mobileshop.controller;

import com.mobileshop.model.Category;
import com.mobileshop.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Admin Category controller - CRUD operations for categories.
 */
public class AdminCategoryServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listCategories(request, response);
                break;
            case "add":
                request.getRequestDispatcher("/admin/category-form.jsp").forward(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteCategory(request, response);
                break;
            default:
                listCategories(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/categories");
            return;
        }

        switch (action) {
            case "add":
                addCategory(request, response);
                break;
            case "edit":
                editCategory(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/categories");
        }
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categories = productService.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/admin/categories.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Category category = productService.getCategoryById(id);
        if (category == null) {
            response.sendRedirect(request.getContextPath() + "/admin/categories");
            return;
        }
        request.setAttribute("category", category);
        request.getRequestDispatcher("/admin/category-form.jsp").forward(request, response);
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        StringBuilder errorMsg = new StringBuilder();
        Category category = productService.createCategory(name, description, errorMsg);

        if (category != null) {
            request.getSession().setAttribute("success", "Category added successfully!");
            response.sendRedirect(request.getContextPath() + "/admin/categories");
        } else {
            request.setAttribute("error", errorMsg.toString());
            request.getRequestDispatcher("/admin/category-form.jsp").forward(request, response);
        }
    }

    private void editCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Category category = productService.getCategoryById(id);
        if (category == null) {
            response.sendRedirect(request.getContextPath() + "/admin/categories");
            return;
        }

        category.setName(request.getParameter("name"));
        category.setDescription(request.getParameter("description"));
        category.setStatus(request.getParameter("status"));

        StringBuilder errorMsg = new StringBuilder();
        if (productService.updateCategory(category, errorMsg)) {
            request.getSession().setAttribute("success", "Category updated successfully!");
            response.sendRedirect(request.getContextPath() + "/admin/categories");
        } else {
            request.setAttribute("error", errorMsg.toString());
            request.setAttribute("category", category);
            request.getRequestDispatcher("/admin/category-form.jsp").forward(request, response);
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        StringBuilder errorMsg = new StringBuilder();
        if (productService.deleteCategory(id, errorMsg)) {
            request.getSession().setAttribute("success", "Category deleted successfully!");
        } else {
            request.getSession().setAttribute("error", errorMsg.toString());
        }
        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}
