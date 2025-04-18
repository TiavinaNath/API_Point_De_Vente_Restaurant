package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.endpoint.mapper.IngredientRestMapper;
import edu.hei.school.restaurant.endpoint.rest.CreateIngredientPrice;
import edu.hei.school.restaurant.endpoint.rest.CreateIngredientStockMovement;
import edu.hei.school.restaurant.endpoint.rest.CreateOrUpdateIngredient;
import edu.hei.school.restaurant.endpoint.rest.IngredientRest;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.model.StockMovement;
import edu.hei.school.restaurant.service.IngredientService;
import edu.hei.school.restaurant.service.exception.ClientException;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class IngredientRestController {
    private final IngredientService ingredientService;
    private final IngredientRestMapper ingredientRestMapper;

    @PostMapping("/ingredients")
    public ResponseEntity<Object> addIngredients() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @PutMapping("/ingredients")
    public ResponseEntity<Object> updateIngredients(@RequestBody List<CreateOrUpdateIngredient> ingredientsToCreateOrUpdate) {
        try {
            List<Ingredient> ingredients = ingredientsToCreateOrUpdate.stream()
                    .map(ingredient -> ingredientRestMapper.toModel(ingredient))
                    .toList();
            List<IngredientRest> ingredientsRest = ingredientService.saveAll(ingredients).stream()
                    .map(ingredient -> ingredientRestMapper.toRest(ingredient))
                    .toList();
            return ResponseEntity.ok().body(ingredientsRest);
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredient(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(ingredientRestMapper.toRest(ingredientService.getById(id)));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
                                                 @RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {
        try {
            List<Ingredient> ingredientsByPrices = ingredientService.getIngredientsByPrices(priceMinFilter, priceMaxFilter);
            List<IngredientRest> ingredientRests = ingredientsByPrices.stream()
                    .map(ingredient -> ingredientRestMapper.toRest(ingredient))
                    .toList();
            return ResponseEntity.ok().body(ingredientRests);
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/ingredients/{ingredientId}/prices")
    public ResponseEntity<Object> updateIngredientPrices(@PathVariable Long ingredientId, @RequestBody List<CreateIngredientPrice> ingredientPrices) {
        List<Price> prices = ingredientPrices.stream()
                .map(ingredientPrice ->
                        new Price(ingredientPrice.getId(), ingredientPrice.getPrice(), ingredientPrice.getDateValue()))
                .toList();
        Ingredient ingredient = ingredientService.addPrices(ingredientId, prices);
        IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);
        return ResponseEntity.ok().body(ingredientRest);
    }

    @PutMapping("ingredients/{ingredientId}/stockMovements")
    public ResponseEntity<Object> updateIngredientStockMovement(@PathVariable Long ingredientId, @RequestBody List<CreateIngredientStockMovement> ingredientStockMovements) {
        List<StockMovement> stockMovements = ingredientStockMovements.stream()
                .map(ingredientStockMovement ->
                        new StockMovement(ingredientStockMovement.getId(), ingredientStockMovement.getQuantity(), ingredientStockMovement.getUnit(), ingredientStockMovement.getType(), ingredientStockMovement.getCreationDatetime()))
                .toList();
        System.out.println(stockMovements);
        Ingredient ingredient = ingredientService.addStockMovements(ingredientId, stockMovements);
        IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);
        return ResponseEntity.ok().body(ingredientRest);
    }
}
