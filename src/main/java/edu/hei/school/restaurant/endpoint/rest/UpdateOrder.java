package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateOrder {
    private List<UpdateOrderDishOrder> dishOrders;
    private StatusOrder statusOrder;
}
