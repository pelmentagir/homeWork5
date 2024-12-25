package repositories.interfaces;

import models.RecipeIngredient;

import java.util.List;

public interface RecipeIngredientRepository {
    List<RecipeIngredient> findAllByRecipeId(Long recipeId);
    List<RecipeIngredient> findAllByIngredientId(Long ingredientId);
    void addIngredientToRecipe(Long recipeId, Long ingredientId, double quantity, String unit);
    void removeIngredientFromRecipe(Long recipeId, Long ingredientId);
}
