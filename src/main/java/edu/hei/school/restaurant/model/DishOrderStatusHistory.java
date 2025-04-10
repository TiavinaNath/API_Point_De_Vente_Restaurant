package edu.hei.school.restaurant.model;

import lombok.*;

import java.time.Instant;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class DishOrderStatusHistory {
    private Long id;
    private StatusDishOrder status;
    private Instant creationDateTime;

    public DishOrderStatusHistory(StatusDishOrder status) {
        this.status = status;
        this.creationDateTime = Instant.now();
    }
}
