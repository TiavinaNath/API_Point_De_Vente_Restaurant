package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.IngredientCrudOperations;
import edu.hei.school.restaurant.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientCrudOperations ingredientCrudOperations;

    public List<Ingredient> getAll(Integer page, Integer size) {
        return ingredientCrudOperations.getAll(page, size);
    }

    public Ingredient getById(Long id) {
        return ingredientCrudOperations.findById(id);
    }

    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
        return ingredientCrudOperations.saveAll(ingredients);
    }
}
