package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateDishIngredient {
    private Long id;
    private Double requiredQuantity;
    private Unit unit;
    private String name;

}
