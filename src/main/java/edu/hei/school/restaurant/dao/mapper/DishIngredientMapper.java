package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.dao.operations.IngredientCrudOperations;
import edu.hei.school.restaurant.model.DishIngredient;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Unit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishIngredientMapper implements Function<ResultSet, DishIngredient> {

    private final IngredientCrudOperations ingredientCrudOperations;

    @SneakyThrows
    @Override
    public DishIngredient apply(ResultSet resultSet) {
        Ingredient ingredient = ingredientCrudOperations.findById(resultSet.getLong("id_ingredient"));
        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setId(resultSet.getLong("id"));
        dishIngredient.setRequiredQuantity(resultSet.getDouble("required_quantity"));
        dishIngredient.setUnit(Unit.valueOf(resultSet.getString("unit")));
        dishIngredient.setIngredient(ingredient);

        return dishIngredient;
    }
}
