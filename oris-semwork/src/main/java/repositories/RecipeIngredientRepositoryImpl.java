package repositories;

import models.RecipeIngredient;
import repositories.interfaces.RecipeIngredientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientRepositoryImpl implements RecipeIngredientRepository {
    private final Connection connection;

    private static final String SELECT_BY_RECIPE_ID = "SELECT * FROM recipe_ingredient WHERE recipe_id = ?";
    private static final String SELECT_BY_INGREDIENT_ID = "SELECT * FROM recipe_ingredient WHERE ingredient_id = ?";
    private static final String INSERT_RECIPE_INGREDIENT =
            "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
    private static final String DELETE_RECIPE_INGREDIENT =
            "DELETE FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";

    public RecipeIngredientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<RecipeIngredient> findAllByRecipeId(Long recipeId) {
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_RECIPE_ID)) {
            stmt.setLong(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipeIngredients.add(mapToRecipeIngredient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipeIngredients;
    }

    @Override
    public List<RecipeIngredient> findAllByIngredientId(Long ingredientId) {
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_BY_INGREDIENT_ID)) {
            stmt.setLong(1, ingredientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipeIngredients.add(mapToRecipeIngredient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipeIngredients;
    }

    @Override
    public void addIngredientToRecipe(Long recipeId, Long ingredientId, double quantity, String unit) {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_RECIPE_INGREDIENT)) {
            stmt.setLong(1, recipeId);
            stmt.setLong(2, ingredientId);
            stmt.setDouble(3, quantity);
            stmt.setString(4, unit);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeIngredientFromRecipe(Long recipeId, Long ingredientId) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_RECIPE_INGREDIENT)) {
            stmt.setLong(1, recipeId);
            stmt.setLong(2, ingredientId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private RecipeIngredient mapToRecipeIngredient(ResultSet rs) throws SQLException {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setRecipeId(rs.getLong("recipe_id"));
        recipeIngredient.setIngredientId(rs.getLong("ingredient_id"));
        recipeIngredient.setQuantity(rs.getDouble("quantity"));
        recipeIngredient.setUnit(rs.getString("unit"));
        return recipeIngredient;
    }
}
