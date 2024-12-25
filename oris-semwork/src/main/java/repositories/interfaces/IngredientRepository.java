package repositories.interfaces;

import models.Ingredient;
import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient> {
    Optional<Ingredient> findByName(String name);
}