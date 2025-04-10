package edu.hei.school.restaurant.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateOrderDishOrder {
    private String dishName;
    private Double quantity;
}
