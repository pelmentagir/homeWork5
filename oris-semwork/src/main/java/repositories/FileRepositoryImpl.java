package repositories;

import models.PhotoFile;
import repositories.interfaces.FileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class FileRepositoryImpl implements FileRepository {

    private JdbcTemplate jdbcTemplate;

    public FileRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String SQL_INSERT = "INSERT INTO files (recipe_id, original_file_name, storage_file_name, type, size) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_RECIPE_ID = "SELECT * FROM files WHERE recipe_id = ?";

    private final RowMapper<PhotoFile> fileRowMapper = (rs, rowNum) -> PhotoFile.builder()
            .id(rs.getLong("id"))
            .recipeId(rs.getLong("recipe_id"))
            .originalFileName(rs.getString("original_file_name"))
            .storageFileName(rs.getString("storage_file_name"))
            .type(rs.getString("type"))
            .size(rs.getLong("size"))
            .build();

    @Override
    public void save(PhotoFile file) {
        jdbcTemplate.update(SQL_INSERT,
                file.getRecipeId(),
                file.getOriginalFileName(),
                file.getStorageFileName(),
                file.getType(),
                file.getSize());
    }

    @Override
    public List<PhotoFile> findByRecipeId(Long recipeId) {
        return jdbcTemplate.query(SQL_SELECT_BY_RECIPE_ID, fileRowMapper, recipeId);
    }
}