package edu.hei.school.restaurant.service.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InsufficientIngredientsException extends RuntimeException {
    private final List<String> missingIngredientsDetails;

    public InsufficientIngredientsException(List<String> missingIngredientsDetails) {
        super(String.join("; \n", missingIngredientsDetails));
        this.missingIngredientsDetails = missingIngredientsDetails;
    }

}