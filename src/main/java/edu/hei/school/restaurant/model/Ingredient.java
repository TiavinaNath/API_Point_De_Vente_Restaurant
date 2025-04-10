package edu.hei.school.restaurant.model;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static edu.hei.school.restaurant.model.StockMovementType.IN;
import static edu.hei.school.restaurant.model.StockMovementType.OUT;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Ingredient {
    private Long id;
    private String name;
    @Setter(AccessLevel.PRIVATE)
    private List<Price> prices;
    @Setter(AccessLevel.PRIVATE)
    private List<StockMovement> stockMovements;

    public List<StockMovement> addStockMovements(List<StockMovement> stockMovements) {
        stockMovements.forEach(stockMovement -> stockMovement.setIngredient(this));
        if (getStockMovements() == null || getStockMovements().isEmpty()) {
            setStockMovements(stockMovements);
            return stockMovements;
        }
        getStockMovements().addAll(stockMovements);
        return getStockMovements();
    }

    public List<Price> addPrices(List<Price> prices) {
        prices.forEach(price -> price.setIngredient(this));
        if (getPrices() == null || getPrices().isEmpty()) {
            setPrices(prices);
            return prices;
        }
        getPrices().addAll(prices);
        return getPrices();
    }

    public Double getActualPrice() {
        return findActualPrice().orElse(new Price(0.0)).getAmount();
    }

    public Double getAvailableQuantity() {
        return getAvailableQuantityAt(Instant.now());
    }

    public Double getPriceAt(LocalDate dateValue) {
        return findPriceAt(dateValue).orElse(new Price(0.0)).getAmount();
    }

    public Double getAvailableQuantityAt(Instant datetime) {
        List<StockMovement> stockMovementsBeforeToday = stockMovements.stream()
                .filter(stockMovement ->
                        stockMovement.getCreationDatetime().isBefore(datetime)
                                || stockMovement.getCreationDatetime().equals(datetime))
                .toList();
        double quantity = 0;
        for (StockMovement stockMovement : stockMovementsBeforeToday) {
            if (IN.equals(stockMovement.getMovementType())) {
                quantity += stockMovement.getQuantity();
            } else if (OUT.equals(stockMovement.getMovementType())) {
                quantity -= stockMovement.getQuantity();
            }
        }
        return quantity;
    }

    private Optional<Price> findPriceAt(LocalDate dateValue) {
        return prices.stream()
                .filter(price -> price.getDateValue().equals(dateValue))
                .findFirst();
    }

    private Optional<Price> findActualPrice() {
        return prices.stream().max(Comparator.comparing(Price::getDateValue));
    }
}
