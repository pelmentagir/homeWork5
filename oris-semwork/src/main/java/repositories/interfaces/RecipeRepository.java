package repositories.interfaces;

import models.Recipe;
import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe> {
    List<Recipe> findAllByUserId(Long userId);
    List<Recipe> findAllByCategoryId(Long categoryId);
    List<Recipe> findAllByTitleContaining(String keyword);
    List<Recipe> findTopRecipes();
}
