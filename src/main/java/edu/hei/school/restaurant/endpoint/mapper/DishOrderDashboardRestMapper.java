package edu.hei.school.restaurant.endpoint.mapper;

import edu.hei.school.restaurant.endpoint.rest.DishOrderDashboardRest;
import edu.hei.school.restaurant.endpoint.rest.DishOrderRest;
import edu.hei.school.restaurant.endpoint.rest.DishOrderStatusRest;
import edu.hei.school.restaurant.model.DishOrder;
import edu.hei.school.restaurant.model.StatusDishOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DishOrderDashboardRestMapper {
    private final DishBasicPropertyRestMapper dishBasicPropertyRestMapper;
    private final DishOrderStatusRestMapper dishOrderStatusRestMapper;

    public DishOrderDashboardRest toDashboardRest(DishOrder dishOrder) {

        List<DishOrderStatusRest> dishOrderStatusRests = dishOrder.getDishOrderStatusHistoryList().stream()
                .map(s -> dishOrderStatusRestMapper.toRest(s))
                .filter(e -> e.getStatus().equals(StatusDishOrder.IN_PROGRESS) || e.getStatus().equals(StatusDishOrder.FINISHED))
                .toList();

        return new DishOrderDashboardRest(
                dishOrder.getId(),
                dishBasicPropertyRestMapper.toRest(dishOrder.getDish()),
                dishOrder.getQuantity().intValue(),
                dishOrderStatusRests,
                dishOrder.getOrder().getActualStatus()
        );
    }
}
