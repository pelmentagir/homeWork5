package repositories;

import models.Category;
import repositories.interfaces.CategoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {
    private final Connection connection;

    private static final String INSERT_CATEGORY = "INSERT INTO Category (name) VALUES (?)";
    private static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM Category WHERE id = ?";
    private static final String SELECT_CATEGORY_BY_NAME = "SELECT * FROM Category WHERE name = ?";
    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM Category";
    private static final String UPDATE_CATEGORY = "UPDATE Category SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY_BY_ID = "DELETE FROM Category WHERE id = ?";

    public CategoryRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_CATEGORIES)) {
            while (rs.next()) {
                categories.add(mapToCategory(rs));
            }
        }
        return categories;
    }

    @Override
    public Optional<Category> findById(Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_CATEGORY_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToCategory(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> findByName(String name) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_CATEGORY_BY_NAME)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToCategory(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(Category category) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_CATEGORY)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Category category) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_CATEGORY)) {
            stmt.setString(1, category.getName());
            stmt.setLong(2, category.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void remove(Category category) throws SQLException {
        removeById(category.getId());
    }

    @Override
    public void removeById(Long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_CATEGORY_BY_ID)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Category mapToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        return category;
    }
}
