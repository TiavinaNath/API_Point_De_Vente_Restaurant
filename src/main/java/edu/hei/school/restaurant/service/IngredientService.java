package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.IngredientCrudOperations;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.model.StockMovement;
import edu.hei.school.restaurant.service.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientCrudOperations ingredientCrudOperations;

    public List<Ingredient> getIngredientsByPrices(Double priceMinFilter, Double priceMaxFilter, Integer page, Integer pageSize) {
        if (priceMinFilter != null && priceMinFilter < 0) {
            throw new ClientException("PriceMinFilter " + priceMinFilter + " is negative");
        }
        if (priceMaxFilter != null && priceMaxFilter < 0) {
            throw new ClientException("PriceMaxFilter " + priceMaxFilter + " is negative");
        }
        if (priceMinFilter != null && priceMaxFilter != null) {
            if (priceMinFilter > priceMaxFilter) {
                throw new ClientException("PriceMinFilter " + priceMinFilter + " is greater than PriceMaxFilter " + priceMaxFilter);
            }
        }

        int actualPage = (page == null || page < 0) ? 1: page;
        int actualSize = (pageSize == null || pageSize <= 0) ? 500: pageSize;
        List<Ingredient> ingredients = ingredientCrudOperations.getAll(actualPage, actualSize);

        return ingredients.stream()
                .filter(ingredient -> {
                    if (priceMinFilter == null && priceMaxFilter == null) {
                        return true;
                    }
                    Double unitPrice = ingredient.getActualPrice();
                    if (priceMinFilter != null && priceMaxFilter == null) {
                        return unitPrice >= priceMinFilter;
                    }
                    if (priceMinFilter == null) {
                        return unitPrice <= priceMaxFilter;
                    }
                    return unitPrice >= priceMinFilter && unitPrice <= priceMaxFilter;
                })
                .toList();
    }

    public List<Ingredient> getAll(Integer page, Integer size) {
        return ingredientCrudOperations.getAll(page, size);
    }

    public Ingredient getById(Long id) {
        return ingredientCrudOperations.findById(id);
    }

    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
        return ingredientCrudOperations.saveAll(ingredients);
    }

    public Ingredient addPrices(Long ingredientId, List<Price> pricesToAdd) {
        Ingredient ingredient = ingredientCrudOperations.findById(ingredientId);
        ingredient.addPrices(pricesToAdd);
        List<Ingredient> ingredientsSaved = ingredientCrudOperations.saveAll(List.of(ingredient));
        return ingredientsSaved.getFirst();
    }

    public Ingredient addStockMovements(Long ingredientId, List<StockMovement> stockMovementsToAdd) {
        Ingredient ingredient = ingredientCrudOperations.findById(ingredientId);
        ingredient.addStockMovements(stockMovementsToAdd);
        List<Ingredient> ingredientsSaved = ingredientCrudOperations.saveAll(List.of(ingredient));
        return ingredientsSaved.getFirst();
    }
}
