package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishIngredientRest {
    private IngredientBasicProperty ingredient;
    private Double requiredQuantity;
    private Unit unit;
}
