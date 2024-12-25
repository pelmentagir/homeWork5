package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    private Long recipeId;
    private Long ingredientId;
    private double quantity;
    private String unit;
}