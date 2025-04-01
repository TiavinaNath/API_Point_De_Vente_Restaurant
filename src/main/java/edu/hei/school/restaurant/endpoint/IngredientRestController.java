package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.service.IngredientService;
import edu.hei.school.restaurant.service.exception.ClientException;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class IngredientRestController {
    private final IngredientService ingredientService;

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
                                                 @RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Object> addIngredients() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @PutMapping("/ingredients")
    public ResponseEntity<Object> updateIngredients(@RequestBody List<Ingredient> ingredients) {
        try {
            return ResponseEntity.ok().body(ingredientService.saveAll(ingredients));
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredient(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(ingredientService.getById(id));
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
