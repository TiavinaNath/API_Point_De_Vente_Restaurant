package edu.hei.school.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DishIngredient {
    private Long id;
    private Ingredient ingredient;
    private Double requiredQuantity;
    private Unit unit;

    public DishIngredient(Ingredient ingredient, Double requiredQuantity, Unit unit) {
        this.ingredient = ingredient;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }

    public DishIngredient(Long id, Ingredient ingredient, Double requiredQuantity) {
        this.id = id;
        this.ingredient = ingredient;
        this.requiredQuantity = requiredQuantity;
    }
}
