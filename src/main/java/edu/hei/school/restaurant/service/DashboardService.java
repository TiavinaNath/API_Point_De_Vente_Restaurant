package edu.hei.school.restaurant.service;

import edu.hei.school.restaurant.dao.operations.DashboardRepository;
import edu.hei.school.restaurant.dto.BestSaleDTO;
import edu.hei.school.restaurant.dto.DishOrderProjection;
import edu.hei.school.restaurant.dto.DishProcessingTimeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    public List<BestSaleDTO> getBestSales(Instant startDate, Instant endDate, int top) {
        return dashboardRepository.getBestSales(startDate, endDate, top);
    }
    public DishProcessingTimeDTO getDishProcessingTime(Long dishId, Instant start, Instant end, String unit, String aggregation) {
        String unitFinal = unit == null ? "seconds" : unit.toLowerCase();
        String aggregationFinal = aggregation == null ? "AVERAGE" : aggregation.toUpperCase();
        return dashboardRepository.getDishProcessingTime(dishId, start, end, unitFinal, aggregationFinal);
    }
    public List<BestSaleDTO> getBestSalesJava(Instant start, Instant end, int top) {
        List<DishOrderProjection> orders = dashboardRepository.getFinishedDishOrdersBetween(start, end);

        Map<Long, BestSaleDTO> aggregation = new HashMap<>();

        for (DishOrderProjection d : orders) {
            aggregation.compute(d.dishId(), (id, current) -> {
                if (current == null) {
                    return new BestSaleDTO(d.dishName(), d.quantity(), d.quantity() * d.unitPrice());
                } else {
                    current.setTotalQuantity(current.getTotalQuantity() + d.quantity());
                    current.setTotalRevenue(current.getTotalRevenue() + d.quantity() * d.unitPrice());
                    return current;
                }
            });
        }

        return aggregation.values().stream()
                .sorted(Comparator.comparing(BestSaleDTO::getTotalQuantity).reversed())
                .limit(top)
                .toList();
    }

}
