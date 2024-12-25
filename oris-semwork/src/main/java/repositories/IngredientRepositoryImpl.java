package repositories;

import models.Ingredient;
import repositories.interfaces.IngredientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientRepositoryImpl implements IngredientRepository {
    private final Connection connection;

    private static final String INSERT_INGREDIENT = "INSERT INTO Ingredient (name) VALUES (?)";
    private static final String SELECT_INGREDIENT_BY_ID = "SELECT * FROM Ingredient WHERE id = ?";
    private static final String SELECT_INGREDIENT_BY_NAME = "SELECT * FROM Ingredient WHERE name = ?";
    private static final String SELECT_ALL_INGREDIENTS = "SELECT * FROM Ingredient";
    private static final String UPDATE_INGREDIENT = "UPDATE Ingredient SET name = ? WHERE id = ?";
    private static final String DELETE_INGREDIENT_BY_ID = "DELETE FROM Ingredient WHERE id = ?";

    public IngredientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Ingredient> findAll() throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_INGREDIENTS)) {
            while (rs.next()) {
                ingredients.add(mapToIngredient(rs));
            }
        }
        return ingredients;
    }

    @Override
    public Optional<Ingredient> findById(Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_INGREDIENT_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToIngredient(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ingredient> findByName(String name) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_INGREDIENT_BY_NAME)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToIngredient(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void save(Ingredient ingredient) {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_INGREDIENT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ingredient.getName());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving ingredient failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ingredient.setId(generatedKeys.getLong(1));
                    System.out.println("Saved ingredient ID: " + ingredient.getId());
                } else {
                    throw new SQLException("Saving ingredient failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving ingredient", e);
        }
    }

    @Override
    public void update(Ingredient ingredient) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_INGREDIENT)) {
            stmt.setString(1, ingredient.getName());
            stmt.setLong(2, ingredient.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void remove(Ingredient ingredient) throws SQLException {
        removeById(ingredient.getId());
    }

    @Override
    public void removeById(Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_INGREDIENT_BY_ID)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Ingredient mapToIngredient(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getLong("id"));
        ingredient.setName(rs.getString("name"));
        return ingredient;
    }
}
