package com.mobileshop.controller;

import com.mobileshop.model.WishlistItem;
import com.mobileshop.service.WishlistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Wishlist controller - add/remove items, view wishlist.
 * Uses session for user identification.
 */
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        switch (action) {
            case "list":
                listWishlist(request, response, userId);
                break;
            case "add":
                addToWishlist(request, response, userId);
                break;
            case "remove":
                removeFromWishlist(request, response, userId);
                break;
            default:
                listWishlist(request, response, userId);
        }
    }

    private void listWishlist(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        List<WishlistItem> items = wishlistService.getWishlistByUserId(userId);
        request.setAttribute("wishlistItems", items);
        request.getRequestDispatcher("/user/wishlist.jsp").forward(request, response);
    }

    private void addToWishlist(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        wishlistService.addToWishlist(userId, productId);
        request.getSession().setAttribute("success", "Product added to wishlist!");
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/products");
    }

    private void removeFromWishlist(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        wishlistService.removeFromWishlist(userId, productId);
        request.getSession().setAttribute("success", "Product removed from wishlist.");
        response.sendRedirect(request.getContextPath() + "/wishlist?action=list");
    }
}
