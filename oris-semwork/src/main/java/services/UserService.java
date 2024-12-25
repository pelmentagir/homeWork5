package services;

import models.User;
import repositories.UserRepositoryImpl;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class UserService {
    private final UserRepositoryImpl userRepository;

    public UserService(Connection connection) {
        this.userRepository = new UserRepositoryImpl(connection);
    }

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    public boolean register(User user) {
        try (Connection connection = DBConnection.getConnection()) {
            UserRepositoryImpl userRepository = new UserRepositoryImpl(connection);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(User user) {
        try {
            userRepository.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

}
