package edu.hei.school.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BestSaleDTO {
    private String dishName;
    private Double totalQuantity;
    private Double totalRevenue;
}