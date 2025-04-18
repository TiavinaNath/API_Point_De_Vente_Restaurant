package edu.hei.school.restaurant.model;

import lombok.*;

import java.time.Instant;
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
                .orElse(new DishOrderStatusHistory(StatusDishOrder.CREATED))
                .getStatus();
    }

    public double getTotalPrice() {
        return dish.getPrice() * quantity;
    }

    public DishOrderStatusHistory updateStatus(DishOrderStatusHistory newStatusHistory) {
        StatusDishOrder currentStatus = this.getActualStatus();
        StatusDishOrder newStatus = newStatusHistory.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new RuntimeException("Transition de statut invalide : " + currentStatus + " -> " + newStatus);
        }

        newStatusHistory.setCreationDateTime(Instant.now());
        this.getDishOrderStatusHistoryList().add(newStatusHistory);

        return newStatusHistory;
    }

    private boolean isValidStatusTransition(StatusDishOrder currentStatus, StatusDishOrder newStatus) {
        switch (currentStatus) {
            case CREATED:
                return newStatus == StatusDishOrder.CONFIRMED;
            case CONFIRMED:
                return newStatus == StatusDishOrder.IN_PROGRESS;
            case IN_PROGRESS:
                return newStatus == StatusDishOrder.FINISHED;
            case FINISHED:
                return newStatus == StatusDishOrder.DELIVERED;
            case DELIVERED:
                return false;
            default:
                return false;
        }
    }

}
