package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.dao.operations.IngredientCrudOperations;
import edu.hei.school.restaurant.endpoint.rest.*;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IngredientRestMapper {
    @Autowired private PriceRestMapper priceRestMapper;
    @Autowired private StockMovementRestMapper stockMovementRestMapper;
    @Autowired private IngredientCrudOperations ingredientCrudOperations;

    public IngredientRest toRest(Ingredient ingredient) {
        List<PriceRest> prices = ingredient.getPrices().stream()
                .map(price -> priceRestMapper.apply(price)).toList();
        List<StockMovementRest> stockMovementRests = ingredient.getStockMovements().stream()
                .map(stockMovement -> stockMovementRestMapper.apply(stockMovement))
                .toList();
        return new IngredientRest(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getActualPrice(),
                ingredient.getAvailableQuantity(),
                prices,
                stockMovementRests
        );
    }

    public Ingredient toModel(CreateOrUpdateIngredient newIngredient) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(newIngredient.getId());
        ingredient.setName(newIngredient.getName());
        try {
            Ingredient existingIngredient = ingredientCrudOperations.findById(newIngredient.getId());
            ingredient.addPrices(existingIngredient.getPrices());
            ingredient.addStockMovements(existingIngredient.getStockMovements());
        } catch (NotFoundException e) {
            ingredient.addPrices(new ArrayList<>());
            ingredient.addStockMovements(new ArrayList<>());
        }
        return ingredient;
    }

    @Component
    @RequiredArgsConstructor
    public static class DishRestMapper {

        private final DishIngredientRestMapper dishIngredientRestMapper;

        public DishRest toRest (Dish dish) {
            List<DishIngredientRest> dishIngredients = dish.getDishIngredients().stream()
                    .map(dishIngredient -> dishIngredientRestMapper.apply(dishIngredient))
                    .toList();

            return new DishRest(
                    dish.getId(),
                    dish.getName(),
                    dish.getAvailableQuantity(),
                    dish.getPrice(),
                    dishIngredients
            );
        }
    }
}
