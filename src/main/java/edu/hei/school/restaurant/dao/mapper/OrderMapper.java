package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.dao.operations.DishOrderCrudOperations;
import edu.hei.school.restaurant.dao.operations.OrderStatusHistoryCrudOperations;
import edu.hei.school.restaurant.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderMapper implements Function<ResultSet, Order> {

    private final DishOrderCrudOperations dishOrderCrudOperations;
    private final OrderStatusHistoryCrudOperations orderStatusHistoryCrudOperations;

    @SneakyThrows
    @Override
    public Order apply(ResultSet resultSet) {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setReference(resultSet.getString("reference"));
        order.setCreationDateTime(resultSet.getTimestamp("creation_date_time").toInstant());
        order.setDishOrderList(dishOrderCrudOperations.getDishOrdersByOrderId(resultSet.getLong("id")));
        order.setOrderStatusHistoryList(orderStatusHistoryCrudOperations.getOrderStatusHistoryByIdOrder(resultSet.getLong("id")));

        return order;
    }
}
