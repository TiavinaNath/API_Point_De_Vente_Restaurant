package edu.hei.school.restaurant.endpoint.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CreateIngredientPrice {
    private Double amount;
    private LocalDate dateValue;
}
