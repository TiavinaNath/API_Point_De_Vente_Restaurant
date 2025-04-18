package edu.hei.school.restaurant.model;

import lombok.*;

import java.time.Instant;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString(exclude = "ingredient")
public class StockMovement {
    private Long id;
    private Ingredient ingredient;
    private Double quantity;
    private Unit unit;
    private StockMovementType movementType;
    private Instant creationDatetime;

    public StockMovement(Double quantity, Unit unit, StockMovementType movementType) {
        this.quantity = quantity;
        this.unit = unit;
        this.movementType = movementType;
    }

    public StockMovement(Long id, Double quantity, Unit unit, StockMovementType movementType, Instant creationDatetime) {
        this.id = id;
        this.quantity = quantity;
        this.unit = unit;
        this.movementType = movementType;
        this.creationDatetime = creationDatetime;
    }
}
