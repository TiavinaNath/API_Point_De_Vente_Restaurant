package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class OrderRest {
    private Long id;
    private String reference;
    private Double totalAmount;
    private List<DishOrderRest> dishOrders;
    private StatusOrder actualStatus;
}
