package com.mobileshop.util;

import java.util.regex.Pattern;

/**
 * Validation utility class for input validation.
 * Provides methods for validating user inputs like names, emails, phone numbers, etc.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{2,100}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,50}$");
    private static final Pattern PRICE_PATTERN = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");

    /**
     * Checks if a string is null or empty.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates a full name - only letters and spaces allowed.
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates a username - alphanumeric and underscores only.
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validates an email address format.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates a phone number - 10 to 15 digits.
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates a price value.
     */
    public static boolean isValidPrice(String price) {
        return price != null && PRICE_PATTERN.matcher(price.trim()).matches();
    }

    /**
     * Validates a password - minimum 6 characters.
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Validates a rating value (1-5).
     */
    public static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }

    /**
     * Validates a stock value - non-negative integer.
     */
    public static boolean isValidStock(int stock) {
        return stock >= 0;
    }

    /**
     * Validates a quantity value - positive integer.
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    /**
     * Sanitizes input string to prevent XSS.
     */
    public static String sanitize(String input) {
        if (input == null) return null;
        return input.replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;")
                    .replace("&", "&amp;");
    }
}
