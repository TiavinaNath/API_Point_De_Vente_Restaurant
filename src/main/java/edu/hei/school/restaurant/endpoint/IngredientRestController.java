package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.model.Ingredient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class IngredientRestController {
    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getIngredients() {
        return ResponseEntity.ok().body(getIngredientList());
    }

    private static List<Ingredient> getIngredientList() {
        return List.of(
                new Ingredient(1L, "Oeuf", 1000.0, Instant.parse("2025-03-01T00:00:00Z")),
                new Ingredient(2L, "Huile", 10000.0, Instant.parse("2025-03-20T00:00:00Z"))
        );
    }
}
