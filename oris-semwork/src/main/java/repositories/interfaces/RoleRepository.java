package repositories.interfaces;

import models.Role;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role> {
    Optional<Role> findByName(String name);
}
