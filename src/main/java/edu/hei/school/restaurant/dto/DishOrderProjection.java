package edu.hei.school.restaurant.dto;

public record DishOrderProjection(
        Long dishId,
        String dishName,
        double quantity,
        double unitPrice
) {}