package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dto.BestSaleDTO;
import edu.hei.school.restaurant.dto.DishOrderProjection;
import edu.hei.school.restaurant.dto.DishProcessingTimeDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DashboardRepository {
    private final DataSource dataSource;
    @SneakyThrows
    public List<BestSaleDTO> getBestSales(Instant startDate, Instant endDate, int top) {
        List<BestSaleDTO> result = new ArrayList<>();
        String sql = """
            SELECT
               d.name AS dish_name,
               SUM(dish_o.quantity) AS total_quantity,
               SUM(dish_o.quantity * d.price) AS total_amount
            FROM dish_order dish_o
            JOIN dish d ON d.id = dish_o.id_dish
            JOIN "order" o ON o.id = dish_o.id_order
            JOIN order_status_history osh ON osh.id_order = o.id
            WHERE
               osh.status = 'FINISHED'
               AND osh.creation_date_time BETWEEN ? AND ?
            GROUP BY d.name
            ORDER BY total_quantity DESC
            LIMIT ?
        """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.from(startDate));
            ps.setTimestamp(2, Timestamp.from(endDate));
            ps.setInt(3, top);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("dish_name");
                    double quantity = rs.getDouble("total_quantity");
                    double total = rs.getDouble("total_amount");
                    result.add(new BestSaleDTO(name, quantity, total));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get best sales", e);
        }
        return result;
    }
    @SneakyThrows
    public List<DishOrderProjection> getFinishedDishOrdersBetween(Instant start, Instant end) {
        String sql = """
        SELECT 
            d.id AS dish_id,
            d.name AS dish_name,
            doo.quantity AS quantity,
            d.price AS unit_price
        FROM dish_order doo
        JOIN dish d ON d.id = doo.id_dish
        JOIN "order" o ON o.id = doo.id_order
        JOIN order_status_history osh ON osh.id_order = o.id
        WHERE 
            osh.status = 'FINISHED'
            AND osh.creation_date_time BETWEEN ? AND ?
    """;

        List<DishOrderProjection> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.from(start));
            ps.setTimestamp(2, Timestamp.from(end));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new DishOrderProjection(
                            rs.getLong("dish_id"),
                            rs.getString("dish_name"),
                            rs.getDouble("quantity"),
                            rs.getDouble("unit_price")
                    ));
                }
            }
        }

        return result;
    }


    @SneakyThrows
    public DishProcessingTimeDTO getDishProcessingTime(Long dishId, Instant start, Instant end, String unit, String aggregation) {
        String sql = """
            SELECT
                d.id AS dish_id,
                d.name AS dish_name,
                AVG(EXTRACT(EPOCH FROM (fh.creation_date_time - ip.creation_date_time))) AS avg_duration,
                MIN(EXTRACT(EPOCH FROM (fh.creation_date_time - ip.creation_date_time))) AS min_duration,
                MAX(EXTRACT(EPOCH FROM (fh.creation_date_time - ip.creation_date_time))) AS max_duration
            FROM dish_order doo
            JOIN dish d ON d.id = doo.id_dish
            JOIN dish_order_status_history ip ON ip.id_dish_order = doo.id AND ip.status = 'IN_PROGRESS'
            JOIN dish_order_status_history fh ON fh.id_dish_order = doo.id AND fh.status = 'FINISHED'
            WHERE d.id = ?
              AND ip.creation_date_time BETWEEN ? AND ?
              AND fh.creation_date_time BETWEEN ? AND ?
            GROUP BY d.id, d.name
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, dishId);
            ps.setTimestamp(2, Timestamp.from(start));
            ps.setTimestamp(3, Timestamp.from(end));
            ps.setTimestamp(4, Timestamp.from(start));
            ps.setTimestamp(5, Timestamp.from(end));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("dish_id");
                    String name = rs.getString("dish_name");
                    double durationInSeconds = switch (aggregation.toUpperCase()) {
                        case "MIN" -> rs.getDouble("min_duration");
                        case "MAX" -> rs.getDouble("max_duration");
                        default -> rs.getDouble("avg_duration");
                    };

                    // Conversion de la durée selon l’unité
                    double finalDuration = switch (unit.toLowerCase()) {
                        case "minutes" -> durationInSeconds / 60;
                        case "hours" -> durationInSeconds / 3600;
                        default -> durationInSeconds; // secondes
                    };

                    return new DishProcessingTimeDTO(id, name, finalDuration, unit, aggregation.toUpperCase());
                }
            }
        }

        return null;
    }

}
