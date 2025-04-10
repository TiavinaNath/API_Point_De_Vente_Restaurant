package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.DishCrudOperations;
import edu.hei.school.restaurant.dao.operations.IngredientCrudOperations;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.DishIngredient;
import edu.hei.school.restaurant.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishCrudOperations dishCrudOperations;
    private final IngredientCrudOperations ingredientCrudOperations;

    public List<Dish> getDishes () {
        return dishCrudOperations.getAll(1, 500);
    }

    public Dish addDishIngredients(Long dishId, List<DishIngredient> dishIngredients) {
        Dish dish = dishCrudOperations.findById(dishId);
        for(DishIngredient dishIngredient: dishIngredients) {
            Ingredient ingredient = ingredientCrudOperations.findByName(dishIngredient.getIngredient().getName());
            dishIngredient.setIngredient(ingredient);
        }
        dish.addDishIngrendients(dishIngredients);
        List<Dish> dishesSaved = dishCrudOperations.saveAll(List.of(dish));
        return dishesSaved.getFirst();
    }
}
