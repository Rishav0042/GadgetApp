package com.mobileshop.controller;

import com.mobileshop.model.Order;
import com.mobileshop.model.OrderItem;
import com.mobileshop.model.Product;
import com.mobileshop.model.User;
import com.mobileshop.service.OrderService;
import com.mobileshop.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User-facing Order controller - place orders, view order history.
 * Uses session to store cart items.
 */
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "history";

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        switch (action) {
            case "cart":
                showCart(request, response);
                break;
            case "addToCart":
                addToCart(request, response);
                break;
            case "removeFromCart":
                removeFromCart(request, response);
                break;
            case "updateCart":
                updateCart(request, response);
                break;
            case "checkout":
                showCheckout(request, response);
                break;
            case "history":
                orderHistory(request, response, userId);
                break;
            case "detail":
                orderDetail(request, response);
                break;
            default:
                orderHistory(request, response, userId);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        if ("placeOrder".equals(action)) {
            placeOrder(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/orders?action=cart");
        }
    }

    @SuppressWarnings("unchecked")
    private void showCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : cart) {
            total = total.add(item.getSubtotal());
        }

        request.setAttribute("cart", cart);
        request.setAttribute("cartTotal", total);
        request.getRequestDispatcher("/user/cart.jsp").forward(request, response);
    }

    @SuppressWarnings("unchecked")
    private void addToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = 1;
        try { quantity = Integer.parseInt(request.getParameter("quantity")); } catch (NumberFormatException e) { quantity = 1; }
        if (quantity < 1) quantity = 1;

        Product product = productService.getProductById(productId);
        if (product == null || !"active".equals(product.getStatus())) {
            request.getSession().setAttribute("error", "Product not available.");
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        if (product.getStock() < quantity) {
            request.getSession().setAttribute("error", "Not enough stock available.");
            response.sendRedirect(request.getContextPath() + "/products?action=detail&id=" + productId);
            return;
        }

        HttpSession session = request.getSession();
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        // Check if product already in cart
        boolean found = false;
        for (OrderItem item : cart) {
            if (item.getProductId() == productId) {
                int newQty = item.getQuantity() + quantity;
                if (newQty > product.getStock()) newQty = product.getStock();
                item.setQuantity(newQty);
                found = true;
                break;
            }
        }
        if (!found) {
            OrderItem item = new OrderItem(productId, quantity, product.getPrice());
            item.setProductName(product.getName());
            cart.add(item);
        }

        session.setAttribute("cart", cart);
        session.setAttribute("success", "Product added to cart!");
        response.sendRedirect(request.getContextPath() + "/products?action=detail&id=" + productId);
    }

    @SuppressWarnings("unchecked")
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        HttpSession session = request.getSession();
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getProductId() == productId);
            session.setAttribute("cart", cart);
        }
        response.sendRedirect(request.getContextPath() + "/orders?action=cart");
    }

    @SuppressWarnings("unchecked")
    private void updateCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        HttpSession session = request.getSession();
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart != null) {
            for (OrderItem item : cart) {
                if (item.getProductId() == productId) {
                    if (quantity <= 0) {
                        cart.remove(item);
                    } else {
                        item.setQuantity(quantity);
                    }
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }
        response.sendRedirect(request.getContextPath() + "/orders?action=cart");
    }

    @SuppressWarnings("unchecked")
    private void showCheckout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Your cart is empty.");
            showCart(request, response);
            return;
        }

        User user = (User) session.getAttribute("user");
        request.setAttribute("user", user);
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/user/checkout.jsp").forward(request, response);
    }

    @SuppressWarnings("unchecked")
    private void placeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            session.setAttribute("error", "Your cart is empty.");
            response.sendRedirect(request.getContextPath() + "/orders?action=cart");
            return;
        }

        String shippingAddress = request.getParameter("shippingAddress");
        String phone = request.getParameter("phone");

        StringBuilder errorMsg = new StringBuilder();
        int orderId = orderService.placeOrder(userId, shippingAddress, phone, cart, errorMsg);

        if (orderId > 0) {
            session.removeAttribute("cart");
            session.setAttribute("success", "Order placed successfully! Order ID: " + orderId);
            response.sendRedirect(request.getContextPath() + "/orders?action=detail&id=" + orderId);
        } else {
            session.setAttribute("error", errorMsg.toString());
            response.sendRedirect(request.getContextPath() + "/orders?action=checkout");
        }
    }

    private void orderHistory(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/user/order-history.jsp").forward(request, response);
    }

    private void orderDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderService.getOrderById(id);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/orders?action=history");
            return;
        }
        List<OrderItem> items = orderService.getOrderItems(id);
        request.setAttribute("order", order);
        request.setAttribute("items", items);
        request.getRequestDispatcher("/user/order-detail.jsp").forward(request, response);
    }
}
