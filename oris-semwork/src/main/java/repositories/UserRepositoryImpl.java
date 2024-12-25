package repositories;


import models.User;
import repositories.interfaces.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection;

    private static final String INSERT_USER = "INSERT INTO \"User\" (username, email, password, registration_date, role_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM \"User\" WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM \"User\"";
    private static final String SELECT_USER_BY_USERNAME = "SELECT * FROM \"User\" WHERE username = ?";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM \"User\" WHERE email = ?";
    private static final String SELECT_USERS_BY_ROLE =
            "SELECT u.* FROM \"User\" u JOIN Role r ON u.role_id = r.id WHERE r.name = ?";
    private static final String UPDATE_USER = "UPDATE \"User\" SET username = ?, email = ?, password = ?, role_id = ? WHERE id = ?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM \"User\" WHERE id = ?";

    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS)) {
            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_USER_BY_ID)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while finding user by username: " + username);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_USER_BY_EMAIL)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while finding user by email: " + email);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllByRole(String roleName) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_USERS_BY_ROLE)) {
            stmt.setString(1, roleName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void save(User user) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setTimestamp(4, Timestamp.valueOf(user.getRegistrationDate()));
            stmt.setObject(5, user.getRoleId(), Types.INTEGER);
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(User user) {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_USER)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setObject(4, user.getRoleId(), Types.INTEGER);
            stmt.setLong(5, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(User user) {
        removeById(user.getId());
    }

    @Override
    public void removeById(Long id) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_USER_BY_ID)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        user.setRoleId((long) rs.getInt("role_id"));
        return user;
    }
}