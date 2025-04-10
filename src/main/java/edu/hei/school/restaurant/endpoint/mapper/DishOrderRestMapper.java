package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.endpoint.rest.DishOrderRest;
import edu.hei.school.restaurant.model.DishOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishOrderRestMapper{
    private final DishBasicPropertyRestMapper dishBasicPropertyRestMapper;

    public DishOrderRest toRest(DishOrder dishOrder) {
        return new DishOrderRest(
                dishOrder.getId(),
                dishBasicPropertyRestMapper.toRest(dishOrder.getDish()),
                dishOrder.getQuantity(),
                dishOrder.getActualStatus()
        );
    }
}
