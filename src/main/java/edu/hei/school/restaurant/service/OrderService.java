package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.OrderCrudOperations;
import edu.hei.school.restaurant.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderCrudOperations orderCrudOperations;

    public List<Order> getOrders () {
       return orderCrudOperations.getAll(1, 500);
    }

    public Order findOrderByReference(String reference) {
        return orderCrudOperations.findByReference(reference);
    }
}
