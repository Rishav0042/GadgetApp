package com.mobileshop.controller;

import com.mobileshop.model.Category;
import com.mobileshop.model.Product;
import com.mobileshop.model.Review;
import com.mobileshop.service.ProductService;
import com.mobileshop.service.ReviewService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * User-facing Product controller - browse, search, view products.
 */
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listProducts(request, response);
                break;
            case "detail":
                showDetail(request, response);
                break;
            case "search":
                searchProducts(request, response);
                break;
            case "category":
                listByCategory(request, response);
                break;
            default:
                listProducts(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("review".equals(action)) {
            submitReview(request, response);
        } else {
            listProducts(request, response);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productService.getActiveProducts();
        List<Category> categories = productService.getActiveCategories();
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/user/products.jsp").forward(request, response);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productService.getProductById(id);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        List<Review> reviews = reviewService.getReviewsByProductId(id);
        double avgRating = reviewService.getAverageRating(id);

        // Check if product is in wishlist
        HttpSession session = request.getSession(false);
        boolean inWishlist = false;
        if (session != null && session.getAttribute("userId") != null) {
            com.mobileshop.service.WishlistService wishlistService = new com.mobileshop.service.WishlistService();
            inWishlist = wishlistService.isInWishlist((int) session.getAttribute("userId"), id);
        }

        request.setAttribute("product", product);
        request.setAttribute("reviews", reviews);
        request.setAttribute("avgRating", avgRating);
        request.setAttribute("inWishlist", inWishlist);
        request.getRequestDispatcher("/user/product-detail.jsp").forward(request, response);
    }

    private void searchProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Product> products = productService.searchProducts(keyword);
        List<Category> categories = productService.getActiveCategories();
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/user/products.jsp").forward(request, response);
    }

    private void listByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        List<Product> products = productService.getProductsByCategory(categoryId);
        List<Category> categories = productService.getActiveCategories();
        Category currentCategory = productService.getCategoryById(categoryId);
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("currentCategory", currentCategory);
        request.getRequestDispatcher("/user/products.jsp").forward(request, response);
    }

    private void submitReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        int productId = Integer.parseInt(request.getParameter("productId"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");

        StringBuilder errorMsg = new StringBuilder();
        Review review = reviewService.createReview(userId, productId, rating, comment, errorMsg);

        if (review != null) {
            request.getSession().setAttribute("success", "Review submitted successfully!");
        } else {
            request.getSession().setAttribute("error", errorMsg.toString());
        }
        response.sendRedirect(request.getContextPath() + "/products?action=detail&id=" + productId);
    }
}
