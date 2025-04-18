package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.DishCrudOperations;
import edu.hei.school.restaurant.dao.operations.OrderCrudOperations;
import edu.hei.school.restaurant.model.*;
import edu.hei.school.restaurant.service.exception.ClientException;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderCrudOperations orderCrudOperations;
    private final DishCrudOperations dishCrudOperations;

    public List<Order> getOrders () {
       return orderCrudOperations.getAll(1, 500);
    }

    public List<Order> saveOrders(List<Order> orders) {
        return orderCrudOperations.saveAll(orders);
    }

    public Order findOrderByReference(String reference) {
        return orderCrudOperations.findByReference(reference);
    }

    public Order updateDishOrders(String reference, List<DishOrder> dishOrders) {
        Order order = orderCrudOperations.findByReference(reference);
        for(DishOrder dishOrder: dishOrders) {
            Dish dish = dishCrudOperations.findByName(dishOrder.getDish().getName());
            dishOrder.setDish(dish);
        }
        order.updateDishOrders(dishOrders);
        List<Order> ordersSaved = orderCrudOperations.saveAll(List.of(order));
        return ordersSaved.getFirst();
    }

    public Order updateDishOrdersThenConfirm(String reference, List<DishOrder> dishOrders) {
        Order order = orderCrudOperations.findByReference(reference);
        for(DishOrder dishOrder: dishOrders) {
            Dish dish = dishCrudOperations.findByName(dishOrder.getDish().getName());
            dishOrder.setDish(dish);
        }
        order.updateDishOrders(dishOrders);
        order.confirmOrder();
        List<Order> ordersSaved = orderCrudOperations.saveAll(List.of(order));
        return ordersSaved.getFirst();
    }

    public Order updateDishOrderStatusByDishId(String reference, Long dishId) {
        Order order = orderCrudOperations.findByReference(reference);

        DishOrder dishOrder = order.getDishOrderList().stream()
                .filter(d -> d.getDish().getId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Dish with ID " + dishId + " not found in the order"));

        StatusDishOrder currentStatus = dishOrder.getActualStatus();
        StatusDishOrder nextStatus;

        switch (currentStatus) {
            case IN_PROGRESS:
                nextStatus = StatusDishOrder.FINISHED;
                break;
            case FINISHED:
                nextStatus = StatusDishOrder.DELIVERED;
                break;
            default:
                throw new IllegalStateException("Cannot advance status from: " + currentStatus);
        }

        DishOrderStatusHistory newStatusHistory = new DishOrderStatusHistory();
        newStatusHistory.setStatus(nextStatus);
        dishOrder.updateStatus(newStatusHistory);

        Order orderToReturn = orderCrudOperations.saveAll(List.of(order)).getFirst();

        return orderToReturn;
    }



}
