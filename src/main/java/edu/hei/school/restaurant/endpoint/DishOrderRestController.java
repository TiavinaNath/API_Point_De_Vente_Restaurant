package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.endpoint.mapper.DishOrderDashboardRestMapper;
import edu.hei.school.restaurant.endpoint.rest.DishOrderDashboardRest;
import edu.hei.school.restaurant.service.DishOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DishOrderRestController {
    private final DishOrderService dishOrderService;
    private final DishOrderDashboardRestMapper dishOrderDashboardRestMapper;

    @GetMapping("/dishOrderDashboard")
    public ResponseEntity<Object> get () {
        List<DishOrderDashboardRest> dishOrders = dishOrderService.findAllDishOrdersWithStatusInProgressAndFinished()
                .stream()
                .map(e -> dishOrderDashboardRestMapper.toDashboardRest(e))
                .toList();
        return ResponseEntity.ok().body(dishOrders);
    }
}
