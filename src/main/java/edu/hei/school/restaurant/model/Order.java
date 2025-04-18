package edu.hei.school.restaurant.model;

import edu.hei.school.restaurant.service.exception.InsufficientIngredientsException;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
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
                .orElse(new OrderStatusHistory(StatusOrder.CREATED))
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

    private List<String> validateIngredientsAvailability() {
        List<String> missingIngredientsDetails = new ArrayList<>();

        for (DishOrder dishOrder : dishOrderList) {
            Dish dish = dishOrder.getDish();
            Double quantity = dishOrder.getQuantity();

            List<DishIngredient> dishIngredients = dish.getDishIngredients();

            for (DishIngredient dishIngredient : dishIngredients) {
                double required = dishIngredient.getRequiredQuantity() * quantity;
                double available = dishIngredient.getIngredient().getAvailableQuantity();
                if (available < required) {
                    double missingQuantity = required - available;

                    missingIngredientsDetails.add(String.format("Il manque %.2f(%s) %s(s) nécessaire(s) pour fabriquer %.2f %s",
                            missingQuantity,
                            dishIngredient.getUnit(),
                            dishIngredient.getIngredient().getName(),
                            quantity,
                            dish.getName()
                    ));

                }
            }
        }

        return missingIngredientsDetails;
    }

    public void confirmOrder() {
        if (!getActualStatus().equals(StatusOrder.CREATED)) {
            throw new IllegalStateException("La commande doit être en statut CRÉÉ pour être confirmée");
        }

        List<String> missingIngredientsDetails = validateIngredientsAvailability();
        if (!missingIngredientsDetails.isEmpty()) {
            throw new InsufficientIngredientsException(missingIngredientsDetails);
        }

        this.getOrderStatusHistoryList().add(new OrderStatusHistory(StatusOrder.CONFIRMED));

        for (DishOrder dishOrder : dishOrderList) {
            dishOrder.updateStatus(new DishOrderStatusHistory(StatusDishOrder.CONFIRMED));
        }

        System.out.println("Commande validée avec succès !");
    }

    public OrderStatusHistory updateStatus(OrderStatusHistory newStatusHistory) {
        StatusOrder currentStatus = this.getActualStatus();
        StatusOrder newStatus = newStatusHistory.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new RuntimeException("Transition de statut invalide : " + currentStatus + " -> " + newStatus);
        }

        newStatusHistory.setCreationDateTime(Instant.now());
        this.getOrderStatusHistoryList().add(newStatusHistory);

        return newStatusHistory;
    }

    private boolean isValidStatusTransition(StatusOrder currentStatus, StatusOrder newStatus) {
        switch (currentStatus) {
            case CREATED:
                return newStatus == StatusOrder.CONFIRMED;
            case CONFIRMED:
                return newStatus == StatusOrder.IN_PROGRESS;
            case IN_PROGRESS:
                return newStatus == StatusOrder.FINISHED;
            case FINISHED:
                return newStatus == StatusOrder.DELIVERED;
            case DELIVERED:
                return false;
            default:
                return false;
        }
    }

    public List<DishOrder> updateDishOrders(List<DishOrder> dishOrders) {
        dishOrders.forEach(dishOrder -> dishOrder.setOrder(this));
        if (getDishOrderList() == null || getDishOrderList().isEmpty()) {
            setDishOrderList(dishOrders);
            return dishOrders;
        }
        for (DishOrder dishOrder : dishOrders) {
            boolean updated = false;
            for (DishOrder existingDishOrder : getDishOrderList()) {
                if (existingDishOrder.getDish().getName().equals(dishOrder.getDish().getName())) {
                    existingDishOrder.setQuantity(dishOrder.getQuantity());
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                getDishOrderList().add(dishOrder);
            }
        }
        return getDishOrderList();
    }

}
