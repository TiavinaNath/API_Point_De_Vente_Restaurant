package edu.hei.school.restaurant.endpoint.rest;

import edu.hei.school.restaurant.model.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
@Getter
public class DishOrderDashboardRest {
    private Long id;
    private DishBasicProperty dish;
    private Integer quantity;
    private List<DishOrderStatusRest> statusHistories;
    private StatusOrder orderStatus;
}
