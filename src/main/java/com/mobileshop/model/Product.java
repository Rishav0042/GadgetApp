package com.mobileshop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Product model representing a mobile accessory product.
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private int categoryId;
    private String brand;
    private String image;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    // Joined fields
    private String categoryName;

    public Product() {}

    public Product(String name, String description, BigDecimal price, int stock, int categoryId, String brand) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.brand = brand;
        this.status = "active";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public boolean isInStock() { return stock > 0; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stock=" + stock + ", brand='" + brand + "'}";
    }
}
