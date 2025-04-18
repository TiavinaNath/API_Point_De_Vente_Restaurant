package edu.hei.school.restaurant.endpoint;

import edu.hei.school.restaurant.dto.BestSaleDTO;
import edu.hei.school.restaurant.dto.DishProcessingTimeDTO;
import edu.hei.school.restaurant.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardRestController {
    private final DashboardService dashboardService;
    @GetMapping("/bestSales")
    public List<BestSaleDTO> getBestSales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam int top
    ) {
        Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().minusMillis(1);

        return dashboardService.getBestSales(start, end, top);
    }
    @GetMapping("/bestSalesJava")
    public List<BestSaleDTO> getBestSalesJava(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam int top
    ) {
        Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().minusMillis(1);

        return dashboardService.getBestSalesJava(start, end, top);
    }
    @GetMapping("/dishes/{id}/processingTime")
    public DishProcessingTimeDTO getDishProcessingTime(
            @PathVariable("id") Long dishId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) String type
    ) {
        Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().minusMillis(1);
        return dashboardService.getDishProcessingTime(dishId, start, end, unit, type);
    }
}
