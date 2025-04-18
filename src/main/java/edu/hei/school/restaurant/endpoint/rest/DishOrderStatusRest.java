package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.StatusDishOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class DishOrderStatusRest {
    private StatusDishOrder status;
    private Instant creationDateTime;
}
