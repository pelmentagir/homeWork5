package repositories.interfaces;

import models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(String roleName);
}
