package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.model.OrderStatusHistory;
import edu.hei.school.restaurant.model.StatusOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderStatusHistoryCrudOperations {
    private final DataSource dataSource;

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
}
