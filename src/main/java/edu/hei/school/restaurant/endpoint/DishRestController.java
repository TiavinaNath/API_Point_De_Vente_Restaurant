package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.endpoint.mapper.IngredientRestMapper;
import edu.hei.school.restaurant.endpoint.rest.CreateDishIngredient;
import edu.hei.school.restaurant.endpoint.rest.CreateIngredientPrice;
import edu.hei.school.restaurant.endpoint.rest.DishRest;
import edu.hei.school.restaurant.endpoint.rest.IngredientRest;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.DishIngredient;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.service.DishService;
import edu.hei.school.restaurant.service.exception.ClientException;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
public class DishRestController {
    private final DishService dishService;
    private final IngredientRestMapper.DishRestMapper dishRestMapper;

    @GetMapping("/dishes")
    public ResponseEntity<Object> getDishes(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        try {
            List<Dish> dishes = dishService.getDishes();
            List<DishRest> dishRests = dishes.stream()
                    .map(dish -> dishRestMapper.toRest(dish))
                    .toList();
            return ResponseEntity.ok().body(dishRests);
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/dishes/{dishId}/ingredients")
    public ResponseEntity<Object> updateDishIngredients(@PathVariable Long dishId, @RequestBody List<CreateDishIngredient> dishIngredientsToCreate) {
        List<DishIngredient> dishIngredients = dishIngredientsToCreate.stream()
                .map(dishIngredient ->
                        new DishIngredient(new Ingredient(dishIngredient.getName()), dishIngredient.getRequiredQuantity(), dishIngredient.getUnit()))
                .toList();
        Dish dish = dishService.addDishIngredients(dishId, dishIngredients);
        DishRest dishRest = dishRestMapper.toRest(dish);
        return ResponseEntity.ok().body(dishRest);
    }


}
