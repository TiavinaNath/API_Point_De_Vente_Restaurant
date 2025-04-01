package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.StockMovement;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class IngredientRest {
    private Long id;
    private String name;
    private List<PriceRest> prices;
    private List<StockMovementRest> stockMovements;
}
