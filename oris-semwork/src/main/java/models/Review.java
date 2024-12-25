package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long id;
    private Long recipeId;
    private Long userId;
    private String text;
    private int rating;
    private LocalDateTime creationDate;
}