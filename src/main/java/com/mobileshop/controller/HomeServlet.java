package com.mobileshop.controller;

import com.mobileshop.model.Product;
import com.mobileshop.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Home Servlet - Loads featured products for the homepage.
 */
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Load featured products (latest 4 active products)
        List<Product> featuredProducts = productService.getFeaturedProducts(4);
        request.setAttribute("featuredProducts", featuredProducts);

        // Forward to index.jsp
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
