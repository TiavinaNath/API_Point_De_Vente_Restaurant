package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextReference;
import edu.hei.school.restaurant.dao.mapper.DishOrderMapper;
import edu.hei.school.restaurant.dao.mapper.OrderMapper;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.model.DishOrder;
import edu.hei.school.restaurant.model.DishOrderStatusHistory;
import edu.hei.school.restaurant.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderCrudOperations {
    private final DataSource dataSource;
    private final DishOrderMapper dishOrderMapper;
    private final DishOrderStatusHistoryCrudOperations dishOrderStatusHistoryCrudOperations;
    private final DishCrudOperations dishCrudOperations;
    private final OrderStatusHistoryCrudOperations orderStatusHistoryCrudOperations;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();

    public List<DishOrder> getDishOrdersByOrderId(Long idOrder) {
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = "SELECT * FROM dish_order WHERE id_order = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, idOrder);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DishOrder dishOrder = dishOrderMapper.apply(rs);
                    dishOrders.add(dishOrder);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dishOrders;
    }

    @SneakyThrows
    public List<DishOrder> saveAll(List<DishOrder> dishOrders) {
        List<DishOrder> savedDishOrders = new ArrayList<>();
        String sql = """
        INSERT INTO dish_order (id, id_order, id_dish, quantity)
        VALUES (?, ?, ?, ?)
        ON CONFLICT (id) DO UPDATE SET id_order = excluded.id_order, id_dish = excluded.id_dish, quantity = excluded.quantity
        RETURNING id, id_order, id_dish, quantity
        """;

        try (Connection connection = dataSource.getConnection()) {
            for (DishOrder dishOrderToSave : dishOrders) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    long id = dishOrderToSave.getId() == null
                            ? postgresNextReference.nextID("dish_order", connection)
                            : dishOrderToSave.getId();

                    statement.setLong(1, id);
                    statement.setLong(2, dishOrderToSave.getOrder().getId());
                    statement.setLong(3, dishOrderToSave.getDish().getId());
                    statement.setDouble(4, dishOrderToSave.getQuantity());

                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        DishOrder savedDishOrder = dishOrderMapper.apply(resultSet);

                        List<DishOrderStatusHistory> statusHistories = dishOrderStatusHistoryCrudOperations.saveAll(dishOrderToSave.getDishOrderStatusHistoryList(), savedDishOrder.getId());

                        savedDishOrder.setDishOrderStatusHistoryList(statusHistories);
                        savedDishOrders.add(savedDishOrder);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Erreur lors de l'insertion d'un dishOrder", e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion pour saveAll DishOrder", e);
        }

        return savedDishOrders;
    }

    @SneakyThrows
    public List<DishOrder> findAllDishOrdersWithStatusInProgressAndFinished () {
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = """
                SELECT d.id, d.id_dish, d.id_order, d.quantity, o.reference
                FROM dish_order d
                JOIN "order" o ON o.id = d.id_order
                WHERE d.id IN (
                    SELECT id_dish_order
                    FROM dish_order_status_history
                    WHERE status IN ('IN_PROGRESS', 'FINISHED')
                    GROUP BY id_dish_order
                    HAVING COUNT(DISTINCT status) = 2
                );
                """;

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                DishOrder dishOrder = new DishOrder();

                Order order = new Order();
                order.setOrderStatusHistoryList(orderStatusHistoryCrudOperations.getOrderStatusHistoryByIdOrder(resultSet.getLong("id_order")));

                dishOrder.setId(resultSet.getLong("id"));
                dishOrder.setDish(dishCrudOperations.findById(resultSet.getLong("id_dish")));
                dishOrder.setOrder(order);
                dishOrder.setQuantity(resultSet.getDouble("quantity"));
                dishOrder.setDishOrderStatusHistoryList(dishOrderStatusHistoryCrudOperations.getDishOrderStatusHistoryByDishOrderId(resultSet.getLong("id")));
                dishOrders.add(dishOrder);
            }
        }
        return dishOrders;
    }

}
