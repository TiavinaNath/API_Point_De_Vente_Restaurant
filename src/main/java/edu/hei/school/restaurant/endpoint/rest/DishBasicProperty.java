package edu.hei.school.restaurant.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishBasicProperty {
    private Long id;
    private String name;
    private Double actualPrice;
}
