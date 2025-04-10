package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.model.DishOrderStatusHistory;
import edu.hei.school.restaurant.model.StatusDishOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor

public class DishOrderStatusHistoryCrudOperations {
    private final DataSource dataSource;
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
}
