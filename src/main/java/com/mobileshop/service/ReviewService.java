package com.mobileshop.service;

import com.mobileshop.dao.ReviewDAO;
import com.mobileshop.model.Review;
import com.mobileshop.util.ValidationUtil;

import java.util.List;

/**
 * Service layer for Review operations.
 */
public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
    }

    public Review createReview(int userId, int productId, int rating, String comment, StringBuilder errorMsg) {
        if (!ValidationUtil.isValidRating(rating)) {
            errorMsg.append("Rating must be between 1 and 5.");
            return null;
        }
        if (ValidationUtil.isNullOrEmpty(comment)) {
            errorMsg.append("Review comment is required.");
            return null;
        }
        Review review = new Review(userId, productId, rating, comment);
        int id = reviewDAO.createReview(review);
        if (id > 0) {
            review.setId(id);
            return review;
        }
        errorMsg.append("Failed to submit review.");
        return null;
    }

    public List<Review> getReviewsByProductId(int productId) {
        return reviewDAO.getReviewsByProductId(productId);
    }

    public double getAverageRating(int productId) {
        return reviewDAO.getAverageRating(productId);
    }

    public boolean deleteReview(int id) {
        return reviewDAO.deleteReview(id);
    }
}
