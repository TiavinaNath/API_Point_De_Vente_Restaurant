package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.StockMovementType;
import edu.hei.school.restaurant.model.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class CreateIngredientStockMovement {
    private Long id;
    private Double quantity;
    private Unit unit;
    private StockMovementType type;
    private Instant creationDatetime;
}
