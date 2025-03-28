package edu.hei.school.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ingredient {
    private Long id;
    private String name;
    private Double unitPrice;
    private Instant updatedAt;
}
