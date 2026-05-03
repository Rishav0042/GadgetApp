package com.mobileshop.controller;

import com.mobileshop.model.Order;
import com.mobileshop.model.OrderItem;
import com.mobileshop.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Admin Order controller - view and manage orders.
 */
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listOrders(request, response);
                break;
            case "detail":
                showDetail(request, response);
                break;
            case "updateStatus":
                updateStatus(request, response);
                break;
            default:
                listOrders(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("updateStatus".equals(action)) {
            updateStatus(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        }
    }

    private void listOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String statusFilter = request.getParameter("status");
        List<Order> orders;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            orders = orderService.getAllOrders();
            orders.removeIf(o -> !statusFilter.equals(o.getStatus()));
        } else {
            orders = orderService.getAllOrders();
        }
        request.setAttribute("orders", orders);
        request.setAttribute("statusFilter", statusFilter);
        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderService.getOrderById(id);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }
        List<OrderItem> items = orderService.getOrderItems(id);
        request.setAttribute("order", order);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/admin/order-detail.jsp").forward(request, response);
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String status = request.getParameter("status");
        if (orderService.updateOrderStatus(orderId, status)) {
            request.getSession().setAttribute("success", "Order status updated to: " + status);
        } else {
            request.getSession().setAttribute("error", "Failed to update order status.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/orders?action=detail&id=" + orderId);
    }
}
