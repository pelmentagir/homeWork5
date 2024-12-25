package repositories.interfaces;

import models.PhotoFile;
import java.util.List;

public interface FileRepository {
    void save(PhotoFile file);
    List<PhotoFile> findByRecipeId(Long recipeId);
}