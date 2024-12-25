package repositories.interfaces;

import models.Review;
import java.util.List;

public interface ReviewRepository extends CrudRepository<Review> {
    List<Review> findAllByRecipeId(Long recipeId);
    List<Review> findAllByUserId(Long userId);
    double calculateAverageRating(Long recipeId);
}