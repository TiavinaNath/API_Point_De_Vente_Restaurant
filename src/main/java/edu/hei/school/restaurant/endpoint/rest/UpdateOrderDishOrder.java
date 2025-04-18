package edu.hei.school.restaurant.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateOrderDishOrder {
    private Long dishIdentifier;
    private Double quantityOrdered;
}
