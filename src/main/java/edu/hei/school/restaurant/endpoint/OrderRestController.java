package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.endpoint.mapper.OrderRestMapper;
import edu.hei.school.restaurant.endpoint.rest.OrderRest;
import edu.hei.school.restaurant.model.Order;
import edu.hei.school.restaurant.service.OrderService;
import edu.hei.school.restaurant.service.exception.ClientException;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
