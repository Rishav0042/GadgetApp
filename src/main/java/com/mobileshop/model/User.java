package com.mobileshop.model;

import java.sql.Timestamp;

/**
 * User model representing a user entity in the system.
 * Supports both admin and user roles.
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private String role;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {}

    public User(String username, String email, String password, String fullName, String phone, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.role = "user";
        this.status = "pending";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public boolean isAdmin() { return "admin".equals(this.role); }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "', fullName='" + fullName + "', role='" + role + "', status='" + status + "'}";
    }
}
