package services;

import models.Review;
import repositories.interfaces.ReviewRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewsByRecipeId(Long recipeId) {
        return reviewRepository.findAllByRecipeId(recipeId);
    }

    public Optional<Review> getReviewById(Long id) {
        try {
            return reviewRepository.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void addReview(Review review) {
        try {
            reviewRepository.save(review);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding review", e);
        }
    }

    public double calculateAverageRating(Long recipeId) {
        return reviewRepository.calculateAverageRating(recipeId);
    }
}