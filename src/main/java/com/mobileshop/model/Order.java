package com.mobileshop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Order model representing a customer order.
 */
public class Order {
    private int id;
    private int userId;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String phone;
    private Timestamp orderDate;
    private Timestamp updatedAt;
    // Joined fields
    private String userName;
    private String userEmail;

    public Order() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    @Override
    public String toString() {
        return "Order{id=" + id + ", userId=" + userId + ", totalAmount=" + totalAmount + ", status='" + status + "'}";
    }
}
