package com.mobileshop.model;

import java.sql.Timestamp;

/**
 * WishlistItem model representing a product in a user's wishlist.
 */
public class WishlistItem {
    private int id;
    private int userId;
    private int productId;
    private Timestamp createdAt;
    // Joined fields
    private String productName;
    private java.math.BigDecimal productPrice;
    private String productImage;
    private String productBrand;
    private int productStock;

    public WishlistItem() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public java.math.BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(java.math.BigDecimal productPrice) { this.productPrice = productPrice; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }

    public String getProductBrand() { return productBrand; }
    public void setProductBrand(String productBrand) { this.productBrand = productBrand; }

    public int getProductStock() { return productStock; }
    public void setProductStock(int productStock) { this.productStock = productStock; }

    @Override
    public String toString() {
        return "WishlistItem{id=" + id + ", userId=" + userId + ", productId=" + productId + "}";
    }
}
