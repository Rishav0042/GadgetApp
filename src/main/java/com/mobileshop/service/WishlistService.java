package com.mobileshop.service;

import com.mobileshop.dao.WishlistDAO;
import com.mobileshop.model.WishlistItem;

import java.util.List;

/**
 * Service layer for Wishlist operations.
 */
public class WishlistService {

    private final WishlistDAO wishlistDAO;

    public WishlistService() {
        this.wishlistDAO = new WishlistDAO();
    }

    public boolean addToWishlist(int userId, int productId) {
        return wishlistDAO.addToWishlist(userId, productId);
    }

    public boolean removeFromWishlist(int userId, int productId) {
        return wishlistDAO.removeFromWishlist(userId, productId);
    }

    public boolean isInWishlist(int userId, int productId) {
        return wishlistDAO.isInWishlist(userId, productId);
    }

    public List<WishlistItem> getWishlistByUserId(int userId) {
        return wishlistDAO.getWishlistByUserId(userId);
    }

    public int countWishlistItems(int userId) {
        return wishlistDAO.countWishlistItems(userId);
    }
}
