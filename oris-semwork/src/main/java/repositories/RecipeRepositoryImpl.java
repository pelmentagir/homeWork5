package repositories;

import models.Recipe;
import repositories.interfaces.RecipeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeRepositoryImpl implements RecipeRepository {
    private final Connection connection;

    private static final String INSERT_RECIPE = "INSERT INTO Recipe (title, description, user_id, category_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_RECIPE_BY_ID = "SELECT * FROM Recipe WHERE id = ?";
    private static final String SELECT_ALL_RECIPES = "SELECT * FROM Recipe";
    private static final String SELECT_RECIPES_BY_USER_ID = "SELECT * FROM Recipe WHERE user_id = ?";
    private static final String SELECT_RECIPES_BY_CATEGORY_ID = "SELECT * FROM Recipe WHERE category_id = ?";
    private static final String SELECT_RECIPES_BY_TITLE_CONTAINING = "SELECT * FROM Recipe WHERE title LIKE ?";
    private static final String UPDATE_RECIPE = "UPDATE Recipe SET title = ?, description = ?, user_id = ?, category_id = ? WHERE id = ?";
    private static final String DELETE_RECIPE_BY_ID = "DELETE FROM Recipe WHERE id = ?";
    private static final String SELECT_TOP_RECIPES = """
                                                    SELECT r.id, r.title, r.description, r.user_id, r.category_id, r.creation_date, COUNT(rv.id) AS review_count, COALESCE(AVG(rv.rating), 0) AS average_rating
                                                    FROM Recipe r
                                                    LEFT JOIN Review rv ON r.id = rv.recipe_id
                                                    GROUP BY r.id, r.title, r.description, r.user_id, r.category_id, r.creation_date
                                                    ORDER BY average_rating DESC, review_count DESC
                                                    LIMIT 4;
                                                    """;

    public RecipeRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Recipe> findAll() {
        List<Recipe> recipes = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_RECIPES)) {
            while (rs.next()) {
                recipes.add(mapToRecipe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_RECIPE_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToRecipe(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Recipe> findAllByUserId(Long userId) {
        List<Recipe> recipes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_RECIPES_BY_USER_ID)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapToRecipe(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public List<Recipe> findAllByCategoryId(Long categoryId) {
        List<Recipe> recipes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_RECIPES_BY_CATEGORY_ID)) {
            stmt.setLong(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapToRecipe(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public List<Recipe> findAllByTitleContaining(String keyword) {
        List<Recipe> recipes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_RECIPES_BY_TITLE_CONTAINING)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    recipes.add(mapToRecipe(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public List<Recipe> findTopRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_TOP_RECIPES);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Recipe recipe = mapToRecipe(rs);
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching top recipes", e);
        }
        return recipes;
    }

    @Override
    public void save(Recipe recipe) {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_RECIPE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, recipe.getTitle());
            stmt.setString(2, recipe.getDescription());
            stmt.setLong(3, recipe.getUserId());
            stmt.setLong(4, recipe.getCategoryId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving recipe failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    recipe.setId(generatedKeys.getLong(1));
                    System.out.println("Saved recipe ID: " + recipe.getId());
                } else {
                    throw new SQLException("Saving recipe failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving recipe", e);
        }
    }

    @Override
    public void update(Recipe recipe) {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_RECIPE)) {
            stmt.setString(1, recipe.getTitle());
            stmt.setString(2, recipe.getDescription());
            stmt.setLong(3, recipe.getUserId());
            stmt.setLong(4, recipe.getCategoryId());
            stmt.setLong(5, recipe.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Recipe recipe) {
        removeById(recipe.getId());
    }

    @Override
    public void removeById(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_RECIPE_BY_ID)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Recipe mapToRecipe(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getLong("id"));
        recipe.setTitle(rs.getString("title"));
        recipe.setDescription(rs.getString("description"));
        recipe.setUserId(rs.getLong("user_id"));
        recipe.setCategoryId(rs.getLong("category_id"));
        return recipe;
    }
}
