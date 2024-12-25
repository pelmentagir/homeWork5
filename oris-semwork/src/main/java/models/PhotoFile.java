package models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoFile {
    private Long id;
    private Long recipeId;
    private String originalFileName;
    private String storageFileName;
    private String type;
    private Long size;
}