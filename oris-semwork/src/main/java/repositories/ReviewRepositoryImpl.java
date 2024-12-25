package repositories;

import models.Review;
import repositories.interfaces.ReviewRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewRepositoryImpl implements ReviewRepository {
    private final Connection connection;

    private static final String INSERT_REVIEW =
            "INSERT INTO Review (user_id, recipe_id, rating, text) VALUES (?, ?, ?, ?)";
    private static final String SELECT_REVIEW_BY_ID = "SELECT * FROM Review WHERE id = ?";
    private static final String SELECT_ALL_REVIEWS = "SELECT * FROM Review";
    private static final String SELECT_REVIEWS_BY_RECIPE_ID = "SELECT * FROM Review WHERE recipe_id = ?";
    private static final String SELECT_REVIEWS_BY_USER_ID = "SELECT * FROM Review WHERE user_id = ?";
    private static final String CALCULATE_AVERAGE_RATING = "SELECT AVG(rating) AS average_rating FROM Review WHERE recipe_id = ?";
    private static final String UPDATE_REVIEW = "UPDATE Review SET user_id = ?, recipe_id = ?, rating = ?, text = ? WHERE id = ?";
    private static final String DELETE_REVIEW_BY_ID = "DELETE FROM Review WHERE id = ?";

    public ReviewRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Review> findAll() {
        List<Review> reviews = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_REVIEWS)) {
            while (rs.next()) {
                reviews.add(mapToReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public Optional<Review> findById(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_REVIEW_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToReview(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(Review review) {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_REVIEW)) {
            stmt.setLong(1, review.getUserId());
            stmt.setLong(2, review.getRecipeId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getText());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Review review) {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_REVIEW)) {
            stmt.setLong(1, review.getUserId());
            stmt.setLong(2, review.getRecipeId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getText());
            stmt.setLong(5, review.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Review review) {
        removeById(review.getId());
    }

    @Override
    public void removeById(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_REVIEW_BY_ID)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Review> findAllByRecipeId(Long recipeId) {
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_REVIEWS_BY_RECIPE_ID)) {
            stmt.setLong(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapToReview(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<Review> findAllByUserId(Long userId) {
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_REVIEWS_BY_USER_ID)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapToReview(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public double calculateAverageRating(Long recipeId) {
        try (PreparedStatement stmt = connection.prepareStatement(CALCULATE_AVERAGE_RATING)) {
            stmt.setLong(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average_rating");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Review mapToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setUserId(rs.getLong("user_id"));
        review.setRecipeId(rs.getLong("recipe_id"));
        review.setRating(rs.getInt("rating"));
        review.setText(rs.getString("text"));
        review.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
        return review;
    }
}
