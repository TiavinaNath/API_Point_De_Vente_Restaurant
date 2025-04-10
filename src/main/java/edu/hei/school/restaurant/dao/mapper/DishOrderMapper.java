package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.dao.operations.DishCrudOperations;
import edu.hei.school.restaurant.dao.operations.DishOrderStatusHistoryCrudOperations;
import edu.hei.school.restaurant.model.DishOrder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DishOrderMapper implements Function<ResultSet, DishOrder> {
    private final DishCrudOperations dishCrudOperations;
    private final DishOrderStatusHistoryCrudOperations dishOrderStatusHistoryCrudOperations;
    @SneakyThrows
    @Override
    public DishOrder apply(ResultSet rs) {
        DishOrder dishOrder = new DishOrder();
        dishOrder.setId(rs.getLong("id"));
        dishOrder.setDish(dishCrudOperations.findById(rs.getLong("id_dish")));
        dishOrder.setQuantity(rs.getDouble("quantity"));
        dishOrder.setDishOrderStatusHistoryList((dishOrderStatusHistoryCrudOperations.getDishOrderStatusHistoryByDishOrderId(rs.getLong("id"))));

    return dishOrder;
    }
}
