package repositories.interfaces;

import models.Category;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category> {
    Optional<Category> findByName(String name);
}
