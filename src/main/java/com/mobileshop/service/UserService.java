package com.mobileshop.service;

import com.mobileshop.dao.UserDAO;
import com.mobileshop.model.User;
import com.mobileshop.util.ValidationUtil;

import java.util.List;

/**
 * Service layer for User operations.
 * Contains business logic and validation for user management.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Registers a new user with validation.
     * @return User object if successful, null if failed
     */
    public User registerUser(String username, String email, String password, String fullName, String phone, String address, StringBuilder errorMsg) {
        // Validate all fields
        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password) || ValidationUtil.isNullOrEmpty(fullName) || ValidationUtil.isNullOrEmpty(phone)) {
            errorMsg.append("All required fields must be filled.");
            return null;
        }
        if (!ValidationUtil.isValidUsername(username)) {
            errorMsg.append("Username must be 3-50 characters (letters, numbers, underscores only).");
            return null;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            errorMsg.append("Please enter a valid email address.");
            return null;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            errorMsg.append("Password must be at least 6 characters long.");
            return null;
        }
        if (!ValidationUtil.isValidName(fullName)) {
            errorMsg.append("Full name must contain only letters and spaces (2-100 characters).");
            return null;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            errorMsg.append("Phone number must be 10-15 digits.");
            return null;
        }
        // Check for duplicates
        if (userDAO.getUserByUsername(username) != null) {
            errorMsg.append("Username already exists. Please choose a different one.");
            return null;
        }
        if (userDAO.getUserByEmail(email) != null) {
            errorMsg.append("Email address is already registered.");
            return null;
        }
        if (userDAO.getUserByPhone(phone) != null) {
            errorMsg.append("Phone number is already registered.");
            return null;
        }
        // Create user
        User user = new User(username, email, password, fullName, phone, address);
        int id = userDAO.createUser(user);
        if (id > 0) {
            user.setId(id);
            return user;
        }
        errorMsg.append("Registration failed. Please try again.");
        return null;
    }

    /**
     * Authenticates a user.
     */
    public User authenticateUser(String username, String password, StringBuilder errorMsg) {
        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(password)) {
            errorMsg.append("Username and password are required.");
            return null;
        }
        User user = userDAO.authenticateUser(username, password);
        if (user == null) {
            errorMsg.append("Invalid username or password, or your account is not yet approved.");
            return null;
        }
        return user;
    }

    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<User> getPendingUsers() {
        return userDAO.getPendingUsers();
    }

    public boolean approveUser(int userId) {
        return userDAO.updateUserStatus(userId, "active");
    }

    public boolean rejectUser(int userId) {
        return userDAO.updateUserStatus(userId, "inactive");
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean updatePassword(int userId, String currentPassword, String newPassword, StringBuilder errorMsg) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            errorMsg.append("User not found.");
            return false;
        }
        if (!com.mobileshop.util.EncryptionUtil.verifyPassword(currentPassword, user.getPassword())) {
            errorMsg.append("Current password is incorrect.");
            return false;
        }
        if (!ValidationUtil.isValidPassword(newPassword)) {
            errorMsg.append("New password must be at least 6 characters long.");
            return false;
        }
        return userDAO.updatePassword(userId, newPassword);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }

    public int countUsers() {
        return userDAO.countUsers();
    }
}
