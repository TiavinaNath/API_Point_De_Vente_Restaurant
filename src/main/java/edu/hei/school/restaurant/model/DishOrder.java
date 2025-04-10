package edu.hei.school.restaurant.model;

import lombok.*;

import java.util.Comparator;
import java.util.List;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DishOrder {
    private Long id;
    private Order order;
    private Dish dish;
    private Double quantity;
    private List<DishOrderStatusHistory> dishOrderStatusHistoryList;

    public DishOrder(Dish dish, Double quantity) {
        this.dish = dish;
        this.quantity = quantity;
        this.dishOrderStatusHistoryList = List.of(new DishOrderStatusHistory(StatusDishOrder.CREATED));
    }

    public StatusDishOrder getActualStatus() {
        return dishOrderStatusHistoryList.stream()
                .max(Comparator.comparing(DishOrderStatusHistory::getCreationDateTime))
                .orElseThrow().getStatus();
    }

    public double getTotalPrice() {
        return dish.getPrice() * quantity;
    }

}
