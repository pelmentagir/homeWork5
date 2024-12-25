package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private Long categoryId;
    private LocalDateTime creationDate;
}