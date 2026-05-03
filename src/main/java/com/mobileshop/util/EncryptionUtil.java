package com.mobileshop.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Encryption utility class for password hashing and verification.
 * Uses BCrypt for secure password encryption.
 */
public class EncryptionUtil {

    /**
     * Hashes a plain text password using BCrypt.
     * @param plainPassword the password to hash
     * @return the BCrypt hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    /**
     * Verifies a plain text password against a BCrypt hashed password.
     * @param plainPassword the password to verify
     * @param hashedPassword the stored BCrypt hash
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
