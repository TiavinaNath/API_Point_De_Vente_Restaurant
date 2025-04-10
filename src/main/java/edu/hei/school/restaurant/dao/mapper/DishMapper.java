package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.dao.operations.DishIngredientCrudOperations;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.DishIngredient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishMapper implements Function<ResultSet, Dish> {
    private final DishIngredientCrudOperations dishIngredientCrudOperations;

    @SneakyThrows
    @Override
    public Dish apply(ResultSet resultSet) {
        List<DishIngredient> dishIngredients = dishIngredientCrudOperations.findByIdDish(resultSet.getLong("id"));
        Dish dish = new Dish();
        dish.setId(resultSet.getLong("id"));
        dish.setName(resultSet.getString("name"));
        dish.setPrice(resultSet.getDouble("price"));
        dish.setDishIngredients(dishIngredients);

        return dish;
    }
}
