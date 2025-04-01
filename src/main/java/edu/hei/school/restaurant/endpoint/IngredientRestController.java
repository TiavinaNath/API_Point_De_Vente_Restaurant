package edu.hei.school.restaurant.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class IngredientRestController {
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
    public ResponseEntity<Object> updateIngredients() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredient(@PathVariable Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
