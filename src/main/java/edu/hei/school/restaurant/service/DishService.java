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

    public List<Dish> getDishes (Integer page, Integer pageSize) {
        int actualPage = (page == null || page < 0) ? 1: page;
        int actualSize = (pageSize == null || pageSize <= 0) ? 500: pageSize;
        return dishCrudOperations.getAll(actualPage, actualSize);

    }

    public Dish addDishIngredients(Long dishId, List<DishIngredient> dishIngredients) {
        Dish dish = dishCrudOperations.findById(dishId);
        for(DishIngredient dishIngredient: dishIngredients) {
            Ingredient ingredient = ingredientCrudOperations.findByName(dishIngredient.getIngredient().getName());
            if (ingredient == null) {
                List<Ingredient> ingredientToSave = List.of(new Ingredient(
                        dishIngredient.getId(),
                        dishIngredient.getIngredient().getName()
                ));
                ingredient = ingredientCrudOperations.saveAll(ingredientToSave).getFirst();
            }
            System.out.println(ingredient);
            dishIngredient.setIngredient(ingredient);
            System.out.println(dishIngredient);
        }

        dish.addDishIngrendients(dishIngredients);
        System.out.println(dish);
        List<Dish> dishesSaved = dishCrudOperations.saveAll(List.of(dish));
        return dishesSaved.getFirst();
    }
}
