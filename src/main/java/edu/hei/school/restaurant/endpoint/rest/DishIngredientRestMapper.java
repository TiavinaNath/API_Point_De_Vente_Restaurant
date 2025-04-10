package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.endpoint.mapper.IngredientRestMapper;
import edu.hei.school.restaurant.model.DishIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishIngredientRestMapper implements Function<DishIngredient, DishIngredientRest> {

    private final IngredientRestMapper ingredientRestMapper;

    @Override
    public DishIngredientRest apply(DishIngredient dishIngredient) {
        IngredientRest ingredientRest = ingredientRestMapper.toRest(dishIngredient.getIngredient());
        IngredientBasicProperty ingredientBasicProperty = new IngredientBasicProperty(
                ingredientRest.getId(),
                ingredientRest.getName(),
                ingredientRest.getActualPrice(),
                ingredientRest.getAvailableQuantity()
        );
        return new DishIngredientRest(
                ingredientBasicProperty,
                dishIngredient.getRequiredQuantity(),
                dishIngredient.getUnit()
        );
    }
}
