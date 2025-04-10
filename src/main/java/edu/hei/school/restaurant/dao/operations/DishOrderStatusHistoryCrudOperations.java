package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextReference;
import edu.hei.school.restaurant.model.DishOrderStatusHistory;
import edu.hei.school.restaurant.model.OrderStatusHistory;
import edu.hei.school.restaurant.model.StatusDishOrder;
import edu.hei.school.restaurant.model.StatusOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor

public class DishOrderStatusHistoryCrudOperations {
    private final DataSource dataSource;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();
    public List<DishOrderStatusHistory> getDishOrderStatusHistoryByDishOrderId(long idDishOrder) {
        List<DishOrderStatusHistory> dishOrderStatusHistories = new ArrayList<>();
        String sql = "select * from dish_order_status_history where id_dish_order = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, idDishOrder);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DishOrderStatusHistory status = new DishOrderStatusHistory();
                    status.setId(rs.getLong("id"));
                    status.setStatus(StatusDishOrder.valueOf(rs.getString("status")));
                    status.setCreationDateTime(rs.getTimestamp("creation_date_time").toInstant());
                    dishOrderStatusHistories.add(status);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dishOrderStatusHistories;
    }

    public List<DishOrderStatusHistory> saveAll(List<DishOrderStatusHistory> histories, Long idDishOrder) {
        List<DishOrderStatusHistory> saved = new ArrayList<>();
        String sql = """
                INSERT INTO dish_order_status_history (id, id_dish_order, status, creation_date_time)
                VALUES (?, ?, cast(? as status_dish_order), ?)
                ON CONFLICT (id) DO UPDATE\s
                SET status = excluded.status, creation_date_time = excluded.creation_date_time
                RETURNING id, id_dish_order, status, creation_date_time
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (DishOrderStatusHistory history : histories) {
                long id = history.getId() == null
                        ? postgresNextReference.nextID("order_status_history", connection)
                        : history.getId();

                statement.setLong(1, id);
                statement.setLong(2, idDishOrder);
                statement.setString(3, history.getStatus().name());
                statement.setTimestamp(4, Timestamp.from(history.getCreationDateTime()));
                statement.addBatch();
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishOrderStatusHistory status = new DishOrderStatusHistory();
                    status.setId(resultSet.getLong("id"));
                    status.setStatus(StatusDishOrder.valueOf(resultSet.getString("status")));
                    status.setCreationDateTime(resultSet.getTimestamp("creation_date_time").toInstant());
                    saved.add(status);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde des OrderStatusHistory", e);
        }

        return saved;
    }
}

