package com.mobileshop.model;

import java.sql.Timestamp;

/**
 * Category model representing a product category.
 */
public class Category {
    private int id;
    private String name;
    private String description;
    private String image;
    private String status;
    private Timestamp createdAt;
    private int productCount;

    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "active";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getProductCount() { return productCount; }
    public void setProductCount(int productCount) { this.productCount = productCount; }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "', status='" + status + "'}";
    }
}
