package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.endpoint.rest.DishBasicProperty;
import edu.hei.school.restaurant.model.Dish;
import org.springframework.stereotype.Component;

@Component
public class DishBasicPropertyRestMapper {

    public DishBasicProperty toRest(Dish dish) {
        return new DishBasicProperty(
                dish.getId(),
                dish.getName(),
                dish.getPrice()
        );
    }
}
