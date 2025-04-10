package edu.hei.school.restaurant.model;

import lombok.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Data
@ToString
public class Order {
    private Long id;
    private String reference;
    private Instant creationDateTime;
    private List<DishOrder> dishOrderList;
    private List<OrderStatusHistory> orderStatusHistoryList;

    public Order(Long id, String reference) {
        this.id = id;
        this.reference = reference;
        this.dishOrderList = new ArrayList<>();
        this.orderStatusHistoryList = List.of(new OrderStatusHistory(StatusOrder.CREATED));
    }

    public Order() {
        this.creationDateTime = Instant.now();
        this.dishOrderList = new ArrayList<>();
        this.orderStatusHistoryList = List.of(new OrderStatusHistory(StatusOrder.CREATED));
    }

    public Order(String reference) {
        this.reference = reference;
        this.creationDateTime = Instant.now();
        this.dishOrderList = new ArrayList<>();
        this.orderStatusHistoryList = List.of(new OrderStatusHistory(StatusOrder.CREATED));
    }

    public StatusOrder getActualStatus() {
        return orderStatusHistoryList.stream()
                .max(Comparator.comparing(OrderStatusHistory::getCreationDateTime))
                .orElseThrow()
                .getStatus();
    }

    public double getTotalAmount() {
        return dishOrderList.stream()
                .mapToDouble(e -> e.getDish().getPrice() * e.getQuantity())
                .sum();
    }


    public Map<DishOrder, StatusDishOrder> getDishOrders() {
        if (dishOrderList == null) {
            return new HashMap<>();
        }
        return dishOrderList.stream()
                .collect(Collectors.toMap(
                        dishOrder -> dishOrder,
                        DishOrder::getActualStatus
                ));
    }

}
