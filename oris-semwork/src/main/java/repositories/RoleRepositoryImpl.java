package repositories;

import models.Role;
import repositories.interfaces.RoleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepositoryImpl implements RoleRepository {
    private final Connection connection;

    private static final String INSERT_ROLE = "INSERT INTO Role (name) VALUES (?)";
    private static final String SELECT_ROLE_BY_ID = "SELECT * FROM Role WHERE id = ?";
    private static final String SELECT_ROLE_BY_NAME = "SELECT * FROM Role WHERE name = ?";
    private static final String SELECT_ALL_ROLES = "SELECT * FROM Role";
    private static final String UPDATE_ROLE = "UPDATE Role SET name = ? WHERE id = ?";
    private static final String DELETE_ROLE_BY_ID = "DELETE FROM Role WHERE id = ?";

    public RoleRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Role> findAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_ROLES)) {
            while (rs.next()) {
                roles.add(mapToRole(rs));
            }
        }
        return roles;
    }

    @Override
    public Optional<Role> findById(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ROLE_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToRole(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> findByName(String name) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ROLE_BY_NAME)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToRole(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(Role role) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_ROLE)) {
            stmt.setString(1, role.getName());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Role role) {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_ROLE)) {
            stmt.setString(1, role.getName());
            stmt.setLong(2, role.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Role role) {
        removeById(role.getId());
    }

    @Override
    public void removeById(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_ROLE_BY_ID)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Role mapToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        return role;
    }
}