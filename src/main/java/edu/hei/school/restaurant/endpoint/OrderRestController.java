package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.endpoint.mapper.OrderRestMapper;
import edu.hei.school.restaurant.endpoint.rest.IngredientRest;
import edu.hei.school.restaurant.endpoint.rest.OrderRest;
import edu.hei.school.restaurant.endpoint.rest.UpdateDishOrderStatus;
import edu.hei.school.restaurant.endpoint.rest.UpdateOrder;
import edu.hei.school.restaurant.model.*;
import edu.hei.school.restaurant.service.OrderService;
import edu.hei.school.restaurant.service.exception.ClientException;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService orderService;
    private final OrderRestMapper orderRestMapper;

    @GetMapping("/orders")
    public List<OrderRest> getAllOrders() {
        List<OrderRest> orderRests = orderService.getOrders().stream()
                .map(order -> orderRestMapper.toRest(order))
                .toList();
        return orderRests;
    }

    @GetMapping("/orders/{reference}")
    public ResponseEntity<Object> getOrderByReference(@PathVariable String reference) {
        try {
            return ResponseEntity.ok().body(orderRestMapper.toRest(orderService.findOrderByReference(reference)));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/orders/{reference}/dishes")
    public ResponseEntity<Object> updateOrder(@PathVariable String reference, @RequestBody UpdateOrder orderDishOrders) {
        if (!orderDishOrders.getOrderStatus().equals(StatusOrder.CREATED) && !orderDishOrders.getOrderStatus().equals(StatusOrder.CONFIRMED)) {
            return ResponseEntity.badRequest().body("Status should be CREATED or CONFIRMED");
        }
        List<DishOrder> dishOrders = orderDishOrders.getDishes().stream()
                .map(dishOrder -> new DishOrder(
                        new Dish(dishOrder.getDishIdentifier()), dishOrder.getQuantityOrdered()
                ))
                .toList();
        if (orderDishOrders.getOrderStatus().equals(StatusOrder.CONFIRMED)) {
            Order order  = orderService.updateDishOrdersThenConfirm(reference, dishOrders);
            OrderRest orderRest = orderRestMapper.toRest(order);
            return ResponseEntity.ok().body(orderRest);
        } else {
            Order order  = orderService.updateDishOrders(reference, dishOrders);
            OrderRest orderRest = orderRestMapper.toRest(order);
            return ResponseEntity.ok().body(orderRest);
        }
    }

/*    @PutMapping("/orders/{reference}/dishes/{dishId}")
    public ResponseEntity<Object> updateDishOrderStatus(@PathVariable String reference, @PathVariable Long dishId ) {
        try {
            return ResponseEntity.ok().body(orderRestMapper.toRest(orderService.updateDishOrderStatusByDishId(reference, dishId )));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }*/


    @PutMapping("/orders/{reference}/dishes/{dishId}")
    public ResponseEntity<Object> changeDishOrderStatus(
            @PathVariable String reference,
            @PathVariable Long dishId,
            @RequestBody UpdateDishOrderStatus dishOrderStatus
    ) {
        try {
            return ResponseEntity.ok().body(orderRestMapper.toRest(orderService.updateDishOrderStatusByDishId(reference, dishId, dishOrderStatus)));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


@PostMapping("orders/{reference}")
    public ResponseEntity<Object> createOrder(@PathVariable String reference) {
        Order order = new Order(reference);
        return ResponseEntity.ok().body(orderRestMapper.toRest(orderService.saveOrders(List.of(order)).getFirst()));
    }
}
