package com.mobileshop.service;

import com.mobileshop.dao.OrderDAO;
import com.mobileshop.model.Order;
import com.mobileshop.model.OrderItem;
import com.mobileshop.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for Order operations.
 */
public class OrderService {

    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    /**
     * Places a new order with validation.
     */
    public int placeOrder(int userId, String shippingAddress, String phone, List<OrderItem> items, StringBuilder errorMsg) {
        if (ValidationUtil.isNullOrEmpty(shippingAddress)) {
            errorMsg.append("Shipping address is required.");
            return -1;
        }
        if (ValidationUtil.isNullOrEmpty(phone) || !ValidationUtil.isValidPhone(phone)) {
            errorMsg.append("Valid phone number is required (10-15 digits).");
            return -1;
        }
        if (items == null || items.isEmpty()) {
            errorMsg.append("Your cart is empty.");
            return -1;
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : items) {
            totalAmount = totalAmount.add(item.getSubtotal());
        }
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("pending");
        order.setShippingAddress(shippingAddress);
        order.setPhone(phone);
        return orderDAO.createOrder(order, items);
    }

    public Order getOrderById(int id) {
        return orderDAO.getOrderById(id);
    }

    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.getOrdersByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return orderDAO.getOrderItems(orderId);
    }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    public int countOrders() {
        return orderDAO.countOrders();
    }

    public int countOrdersByStatus(String status) {
        return orderDAO.countOrdersByStatus(status);
    }

    public BigDecimal getTotalRevenue() {
        return orderDAO.getTotalRevenue();
    }
}
