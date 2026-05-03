package com.mobileshop.controller;

import com.mobileshop.model.Category;
import com.mobileshop.model.Product;
import com.mobileshop.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Admin Product controller - CRUD operations for products.
 */
@MultipartConfig(maxFileSize = 16177215)
public class AdminProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listProducts(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            default:
                listProducts(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        switch (action) {
            case "add":
                addProduct(request, response);
                break;
            case "edit":
                editProduct(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categories = productService.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/admin/product-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = parseInt(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        Product product = productService.getProductById(id);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        List<Category> categories = productService.getAllCategories();
        request.setAttribute("product", product);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/admin/product-form.jsp").forward(request, response);
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuilder errorMsg = new StringBuilder();
        Product product = extractProductFromRequest(request, errorMsg, null);

        if (product != null && productService.addProduct(product)) {
            request.getSession().setAttribute("success", "Product added successfully!");
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } else {
            request.setAttribute("error", errorMsg.toString());
            request.setAttribute("product", product);
            showAddForm(request, response);
        }
    }

    private void editProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer id = parseInt(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        StringBuilder errorMsg = new StringBuilder();
        Product updatedProduct = extractProductFromRequest(request, errorMsg, existingProduct);

        if (updatedProduct != null && productService.updateProduct(updatedProduct)) {
            request.getSession().setAttribute("success", "Product updated successfully!");
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } else {
            request.setAttribute("error", errorMsg.toString());
            request.setAttribute("product", updatedProduct);
            showEditForm(request, response);
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer id = parseInt(request.getParameter("id"));

        if (id != null && productService.deleteProduct(id)) {
            request.getSession().setAttribute("success", "Product deleted successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to delete product.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/products");
    }

    private Product extractProductFromRequest(HttpServletRequest request, StringBuilder errorMsg, Product product)
            throws IOException, ServletException {

        String name = trim(request.getParameter("name"));
        String description = trim(request.getParameter("description"));
        String brand = trim(request.getParameter("brand"));
        String status = trim(request.getParameter("status"));

        BigDecimal price = parseBigDecimal(request.getParameter("price"));
        Integer stock = parseInt(request.getParameter("stock"));
        Integer categoryId = parseInt(request.getParameter("categoryId"));

        Part imagePart = request.getPart("image");
        byte[] imageBytes = null;

        if (imagePart != null && imagePart.getSize() > 0) {
            InputStream inputStream = imagePart.getInputStream();
            imageBytes = inputStream.readAllBytes();
        }

        if (name.isEmpty()) errorMsg.append("Product name is required. ");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) errorMsg.append("Valid price is required. ");
        if (stock == null || stock < 0) errorMsg.append("Valid stock is required. ");
        if (categoryId == null) errorMsg.append("Valid category is required. ");

        if (errorMsg.length() > 0) return null;

        if (product == null) {
            product = new Product();
            product.setStatus("active");
        }

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategoryId(categoryId);
        product.setBrand(brand);

        if (!status.isEmpty()) {
            product.setStatus(status);
        }

        if (imageBytes != null) {
            String relativeUrl = saveUploadedImage(request, imagePart, imageBytes);
            if (relativeUrl != null) {
                product.setImage(relativeUrl);
            }
        }

        return product;
    }

    private String saveUploadedImage(HttpServletRequest request, Part imagePart, byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        String baseDir = getServletContext().getRealPath("/images/products");
        if (baseDir == null) {
            return null;
        }
        File dir = new File(baseDir);
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }
        String ext = ".jpg";
        if (imagePart != null) {
            try {
                String submitted = imagePart.getSubmittedFileName();
                if (submitted != null && submitted.contains(".")) {
                    ext = submitted.substring(submitted.lastIndexOf('.')).toLowerCase();
                    if (ext.length() > 10) {
                        ext = ".jpg";
                    }
                }
            } catch (Exception ignored) {
                // keep default extension
            }
        }
        String fileName = "p_" + System.currentTimeMillis() + ext;
        try {
            Files.write(Path.of(baseDir, fileName), imageBytes);
            return request.getContextPath() + "/images/products/" + fileName;
        } catch (IOException e) {
            System.err.println("Error saving product image: " + e.getMessage());
            return null;
        }
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return null;
        }
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}