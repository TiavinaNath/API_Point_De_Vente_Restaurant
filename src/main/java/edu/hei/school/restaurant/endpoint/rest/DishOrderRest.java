package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.StatusDishOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishOrderRest {
    private Long id;
    private DishBasicProperty dish;
    private Double quantity;
    private StatusDishOrder actualStatus;
}
