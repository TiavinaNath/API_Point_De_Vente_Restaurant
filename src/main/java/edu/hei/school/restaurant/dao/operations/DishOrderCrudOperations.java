package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.model.DishOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderCrudOperations {
    private final DataSource dataSource;
    private final DishCrudOperations dishCrudOperations;
    private final DishOrderStatusHistoryCrudOperations dishOrderStatusHistoryCrudOperations;

    public List<DishOrder> getDishOrdersByOrderId(Long idOrder) {
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = "SELECT * FROM dish_order WHERE id_order = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, idOrder);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DishOrder dishOrder = new DishOrder();
                    dishOrder.setId(rs.getLong("id"));
                    dishOrder.setDish(dishCrudOperations.findById(rs.getLong("id_dish")));
                    dishOrder.setQuantity(rs.getDouble("quantity"));
                    dishOrder.setDishOrderStatusHistoryList((dishOrderStatusHistoryCrudOperations.getDishOrderStatusHistoryByDishOrderId(rs.getLong("id"))));
                    dishOrders.add(dishOrder);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dishOrders;
    }
}
