package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.endpoint.rest.DishRest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishRestController {
    @GetMapping("/dishes")
    public List<DishRest> getDishes(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
