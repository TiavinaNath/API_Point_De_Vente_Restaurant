package edu.hei.school.restaurant.endpoint.rest;


import edu.hei.school.restaurant.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DishRestMapper {

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
