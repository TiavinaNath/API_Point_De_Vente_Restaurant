package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.DishOrderCrudOperations;
import edu.hei.school.restaurant.model.DishOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishOrderService {
    private final DishOrderCrudOperations dishOrderCrudOperations;

    public List<DishOrder> findAllDishOrdersWithStatusInProgressAndFinished () {
        return dishOrderCrudOperations.findAllDishOrdersWithStatusInProgressAndFinished();
    }

}
