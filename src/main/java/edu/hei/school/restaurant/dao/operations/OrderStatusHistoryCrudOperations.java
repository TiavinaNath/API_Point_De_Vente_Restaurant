package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextReference;
import edu.hei.school.restaurant.model.OrderStatusHistory;
import edu.hei.school.restaurant.model.StatusOrder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderStatusHistoryCrudOperations {
    private final DataSource dataSource;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();

    public List<OrderStatusHistory> getOrderStatusHistoryByIdOrder(Long idOrder) {
        List<OrderStatusHistory> statusHistory = new ArrayList<>();
        String sql = "select * from order_status_history WHERE id_order = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, idOrder);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderStatusHistory status = new OrderStatusHistory();
                    status.setId(rs.getLong("id"));
                    status.setStatus(StatusOrder.valueOf(rs.getString("status")));
                    status.setCreationDateTime(rs.getTimestamp("creation_date_time").toInstant());
                    statusHistory.add(status);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return statusHistory;
    }

    @SneakyThrows
    public List<OrderStatusHistory> saveAll(List<OrderStatusHistory> histories, Long idOrder) {
        List<OrderStatusHistory> saved = new ArrayList<>();
        String sql = """
                INSERT INTO order_status_history (id, id_order, status, creation_date_time)
                VALUES (?, ?, cast(? as status_order), ?)
                ON CONFLICT (id) DO UPDATE\s
                SET status = excluded.status, creation_date_time = excluded.creation_date_time
                RETURNING id, id_order, status, creation_date_time
        """;

        try (Connection connection = dataSource.getConnection()) {
            for (OrderStatusHistory history : histories) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    long id = history.getId() == null
                            ? postgresNextReference.nextID("order_status_history", connection)
                            : history.getId();

                    statement.setLong(1, id);
                    statement.setLong(2, idOrder);
                    statement.setString(3, history.getStatus().name());
                    statement.setTimestamp(4, Timestamp.from(history.getCreationDateTime()));

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            OrderStatusHistory savedHistory = new OrderStatusHistory();
                            savedHistory.setId(resultSet.getLong("id"));
                            savedHistory.setStatus(StatusOrder.valueOf(resultSet.getString("status")));
                            savedHistory.setCreationDateTime(resultSet.getTimestamp("creation_date_time").toInstant());
                            saved.add(savedHistory);
                        }
                    }
                }
            }
        }
        return saved;
    }

}
