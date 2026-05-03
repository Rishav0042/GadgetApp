package com.mobileshop.controller;

import com.mobileshop.model.Order;
import com.mobileshop.model.Product;
import com.mobileshop.model.Category;
import com.mobileshop.service.OrderService;
import com.mobileshop.service.ProductService;
import com.mobileshop.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Admin Dashboard controller.
 */
public class AdminDashboardServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Dashboard statistics
        int totalProducts = productService.countProducts();
        int totalOrders = orderService.countOrders();
        int totalUsers = userService.countUsers();
        int pendingOrders = orderService.countOrdersByStatus("pending");
        java.math.BigDecimal revenue = orderService.getTotalRevenue();
        List<Order> recentOrders = orderService.getAllOrders();
        if (recentOrders.size() > 5) recentOrders = recentOrders.subList(0, 5);

        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("pendingOrders", pendingOrders);
        request.setAttribute("revenue", revenue);
        request.setAttribute("recentOrders", recentOrders);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
