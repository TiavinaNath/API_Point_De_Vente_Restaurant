package edu.hei.school.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DishProcessingTimeDTO {
    private Long dishId;
    private String dishName;
    private Double duration;
    private String unit;
    private String aggregation;
}
