package com.mobileshop.service;

import com.mobileshop.dao.CategoryDAO;
import com.mobileshop.dao.ProductDAO;
import com.mobileshop.model.Category;
import com.mobileshop.model.Product;
import com.mobileshop.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for Product and Category operations.
 */
public class ProductService {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
    }

    // ---- Category Operations ----

    public Category createCategory(String name, String description, StringBuilder errorMsg) {
        if (ValidationUtil.isNullOrEmpty(name)) {
            errorMsg.append("Category name is required.");
            return null;
        }
        if (categoryDAO.getCategoryByName(name) != null) {
            errorMsg.append("Category with this name already exists.");
            return null;
        }
        Category category = new Category(name, description);
        int id = categoryDAO.createCategory(category);
        if (id > 0) {
            category.setId(id);
            return category;
        }
        errorMsg.append("Failed to create category.");
        return null;
    }

    public Category getCategoryById(int id) {
        return categoryDAO.getCategoryById(id);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public List<Category> getActiveCategories() {
        return categoryDAO.getActiveCategories();
    }

    public boolean updateCategory(Category category, StringBuilder errorMsg) {
        if (ValidationUtil.isNullOrEmpty(category.getName())) {
            errorMsg.append("Category name is required.");
            return false;
        }
        Category existing = categoryDAO.getCategoryByName(category.getName());
        if (existing != null && existing.getId() != category.getId()) {
            errorMsg.append("Category with this name already exists.");
            return false;
        }
        return categoryDAO.updateCategory(category);
    }

    public boolean deleteCategory(int id, StringBuilder errorMsg) {
        int productCount = productDAO.countProductsByCategory(id);
        if (productCount > 0) {
            errorMsg.append("Cannot delete category with " + productCount + " products. Remove or reassign products first.");
            return false;
        }
        return categoryDAO.deleteCategory(id);
    }

    // ---- Product Operations ----

    public Product createProduct(String name, String description, String priceStr, String stockStr, int categoryId, String brand, String image, StringBuilder errorMsg) {
        if (ValidationUtil.isNullOrEmpty(name)) {
            errorMsg.append("Product name is required.");
            return null;
        }
        if (!ValidationUtil.isValidPrice(priceStr)) {
            errorMsg.append("Please enter a valid price.");
            return null;
        }
        int stock;
        try {
            stock = Integer.parseInt(stockStr);
            if (!ValidationUtil.isValidStock(stock)) {
                errorMsg.append("Stock must be a non-negative number.");
                return null;
            }
        } catch (NumberFormatException e) {
            errorMsg.append("Please enter a valid stock quantity.");
            return null;
        }
        if (categoryDAO.getCategoryById(categoryId) == null) {
            errorMsg.append("Selected category does not exist.");
            return null;
        }
        Product product = new Product(name, description, new BigDecimal(priceStr), stock, categoryId, brand);
        product.setImage(image);
        int id = productDAO.createProduct(product);
        if (id > 0) {
            product.setId(id);
            return product;
        }
        errorMsg.append("Failed to create product.");
        return null;
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<Product> getActiveProducts() {
        return productDAO.getActiveProducts();
    }

    public List<Product> getFeaturedProducts(int limit) {
        return productDAO.getFeaturedProducts(limit);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        if (ValidationUtil.isNullOrEmpty(keyword)) return getActiveProducts();
        return productDAO.searchProducts(keyword);
    }

    public boolean addProduct(Product product) {
        if (product == null) {
            return false;
        }
        int id = productDAO.createProduct(product);
        if (id > 0) {
            product.setId(id);
            return true;
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int id) {
        return productDAO.deleteProduct(id);
    }

    public int countProducts() {
        return productDAO.countProducts();
    }
}
