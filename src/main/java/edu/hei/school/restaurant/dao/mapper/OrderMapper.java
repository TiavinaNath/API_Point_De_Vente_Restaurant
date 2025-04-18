package edu.hei.school.restaurant.dao.mapper;

import edu.hei.school.restaurant.dao.operations.DishOrderCrudOperations;
import edu.hei.school.restaurant.dao.operations.OrderStatusHistoryCrudOperations;
import edu.hei.school.restaurant.model.DishOrder;
import edu.hei.school.restaurant.model.Order;
import edu.hei.school.restaurant.model.OrderStatusHistory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OrderMapper implements Function<ResultSet, Order> {

    private final DishOrderCrudOperations dishOrderCrudOperations;
    private final OrderStatusHistoryCrudOperations orderStatusHistoryCrudOperations;

    @SneakyThrows
    @Override
    public Order apply(ResultSet resultSet) {
        List<DishOrder> dishOrders = dishOrderCrudOperations.getDishOrdersByOrderId(resultSet.getLong("id")) == null? new ArrayList<>() : dishOrderCrudOperations.getDishOrdersByOrderId(resultSet.getLong("id")) ;
        List<OrderStatusHistory> status = orderStatusHistoryCrudOperations.getOrderStatusHistoryByIdOrder(resultSet.getLong("id")) == null? new ArrayList<>(): orderStatusHistoryCrudOperations.getOrderStatusHistoryByIdOrder(resultSet.getLong("id"))  ;
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setReference(resultSet.getString("reference"));
        order.setCreationDateTime(resultSet.getTimestamp("creation_date_time").toInstant());
        order.setDishOrderList(dishOrders);
        order.setOrderStatusHistoryList(status);

        return order;
    }
}
