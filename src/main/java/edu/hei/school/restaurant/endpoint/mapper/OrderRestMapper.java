package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.endpoint.rest.DishOrderRest;
import edu.hei.school.restaurant.endpoint.rest.OrderRest;
import edu.hei.school.restaurant.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderRestMapper {
    private final DishOrderRestMapper dishOrderRestMapper;

    public OrderRest toRest(Order order) {

        List<DishOrderRest> dishOrderRests = order.getDishOrderList().stream()
                .map(dishOrder -> dishOrderRestMapper.toRest(dishOrder))
                .toList();

        return new OrderRest(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                dishOrderRests,
                order.getActualStatus()
        );
    }
}
