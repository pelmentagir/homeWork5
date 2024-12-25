package services;

import models.Category;
import models.Ingredient;
import models.Recipe;
import models.RecipeIngredient;
import repositories.interfaces.CategoryRepository;
import repositories.interfaces.IngredientRepository;
import repositories.interfaces.RecipeIngredientRepository;
import repositories.interfaces.RecipeRepository;

import java.sql.SQLException;
import java.util.*;

public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    public RecipeService(RecipeRepository recipeRepository,
                         RecipeIngredientRepository recipeIngredientRepository,
                         IngredientRepository ingredientRepository,
                         CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    public void addRecipe(Recipe recipe, List<RecipeIngredient> ingredients) throws SQLException {
        try {
            recipeRepository.save(recipe);

            System.out.println("Saved Recipe ID: " + recipe.getId());

            for (RecipeIngredient ingredient : ingredients) {
                ingredient.setRecipeId(recipe.getId());
                ingredient.setIngredientId(ingredient.getIngredientId());

                System.out.println("Recipe ID: " + ingredient.getRecipeId());
                System.out.println("Ingredient ID: " + ingredient.getIngredientId());
                System.out.println("Quantity: " + ingredient.getQuantity());
                System.out.println("Unit: " + ingredient.getUnit());

                recipeIngredientRepository.addIngredientToRecipe(
                        ingredient.getRecipeId(),
                        ingredient.getIngredientId(),
                        ingredient.getQuantity(),
                        ingredient.getUnit()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding recipe", e);
        }
    }

    public List<Recipe> getUserRecipes(Long userId) {
        return safeExecute(() -> recipeRepository.findAllByUserId(userId), "Error fetching recipes for user");
    }

    public List<Recipe> getAllRecipes() {
        return safeExecute(recipeRepository::findAll, "Error fetching all recipes");
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return safeExecute(() -> recipeRepository.findById(id), "Error while fetching recipe by ID");
    }

    public List<Category> getAllCategories() {
        return safeExecute(categoryRepository::findAll, "Error while fetching all categories");
    }

    public Ingredient findOrCreateIngredient(String name) {
        return safeExecute(() -> {
            Optional<Ingredient> ingredient = ingredientRepository.findByName(name);
            if (ingredient.isPresent()) {
                return ingredient.get();
            } else {
                Ingredient newIngredient = new Ingredient(null, name);
                ingredientRepository.save(newIngredient);
                return newIngredient;
            }
        }, "Error while finding or creating ingredient");
    }

    public void deleteRecipe(Long recipeId) {
        safeExecute(() -> {
            recipeRepository.removeById(recipeId);
            return null;
        }, "Error while deleting recipe");
    }

    private <T> T safeExecute(RepositoryAction<T> action, String errorMessage) {
        try {
            return action.execute();
        } catch (SQLException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }

    public List<RecipeIngredient> getIngredientsByRecipeId(Long recipeId) {
        return safeExecute(() -> recipeIngredientRepository.findAllByRecipeId(recipeId),
                "Error fetching ingredients for recipe ID: " + recipeId);
    }

    public List<Map<String, Object>> getDetailedIngredientsByRecipeId(Long recipeId) throws SQLException {
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findAllByRecipeId(recipeId);
        List<Map<String, Object>> detailedIngredients = new ArrayList<>();

        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            Long ingredientId = recipeIngredient.getIngredientId();
            Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);

            if (ingredientOpt.isPresent()) {
                Ingredient ingredient = ingredientOpt.get();
                Map<String, Object> ingredientDetails = new HashMap<>();
                ingredientDetails.put("name", ingredient.getName());
                ingredientDetails.put("quantity", recipeIngredient.getQuantity());
                ingredientDetails.put("unit", recipeIngredient.getUnit());
                detailedIngredients.add(ingredientDetails);
            }
        }

        return detailedIngredients;
    }

    public List<Recipe> getTopRecipes() {
        return safeExecute(recipeRepository::findTopRecipes, "Error fetching top recipes");
    }

    @FunctionalInterface
    private interface RepositoryAction<T> {
        T execute() throws SQLException;
    }
}