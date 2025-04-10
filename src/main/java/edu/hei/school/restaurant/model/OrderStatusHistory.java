package edu.hei.school.restaurant.model;

import lombok.*;

import java.time.Instant;


@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class OrderStatusHistory {
    private Long id;
    private StatusOrder status;
    private Instant creationDateTime;

    public OrderStatusHistory(StatusOrder status) {
        this.status = status;
        this.creationDateTime = Instant.now();
    }
}
