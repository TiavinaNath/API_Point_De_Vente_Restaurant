package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextReference;
import edu.hei.school.restaurant.dao.mapper.OrderMapper;
import edu.hei.school.restaurant.model.DishOrder;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Order;
import edu.hei.school.restaurant.model.OrderStatusHistory;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCrudOperations {
    private final DataSource dataSource;
    private final OrderMapper orderMapper;
    private final DishOrderCrudOperations dishOrderCrudOperations;
    private final OrderStatusHistoryCrudOperations orderStatusHistoryCrudOperations;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();

    public List<Order> getAll(int page, int size) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select o.id, o.reference, o.creation_date_time from \"order\" o order by o.id asc limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = orderMapper.apply(resultSet) ;
                    orders.add(order);
                }
                return orders;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    public Order findByReference(String reference) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select o.id, o.reference, o.creation_date_time from \"order\" o where o.reference = ?")) {
            statement.setString(1, reference);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return orderMapper.apply(resultSet) ;
                }
                throw new NotFoundException("Order.reference=" + reference + " not found");
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    public List<Order> saveAll(List<Order> orders) {
        List<Order> savedOrders = new ArrayList<>();
        String sql = """
        INSERT INTO "order" (id, reference, creation_date_time)
        VALUES (?, ?, ?)
        ON CONFLICT (id) DO UPDATE SET reference = excluded.reference
        RETURNING id, reference, creation_date_time
        """;

        try (Connection connection = dataSource.getConnection()) {
            for (Order orderToSave : orders) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    long id = orderToSave.getId() == null
                            ? postgresNextReference.nextID("order", connection)
                            : orderToSave.getId();

                    statement.setLong(1, id);
                    statement.setString(2, orderToSave.getReference());
                    statement.setTimestamp(3, Timestamp.from(orderToSave.getCreationDateTime()));

                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        Order savedOrder = orderMapper.apply(resultSet);
                        List<DishOrder> savedDishOrders = dishOrderCrudOperations.saveAll(orderToSave.getDishOrderList().stream()
                                .peek(d -> d.setOrder(savedOrder))
                                .toList());

                        System.out.println(orderToSave.getOrderStatusHistoryList());
                        List<OrderStatusHistory> savedStatusHistories = orderStatusHistoryCrudOperations.saveAll(orderToSave.getOrderStatusHistoryList(), savedOrder.getId());
                        System.out.println(savedStatusHistories);
                        savedOrder.setDishOrderList(savedDishOrders);
                        savedOrder.setOrderStatusHistoryList(savedStatusHistories);
                        savedOrders.add(savedOrder);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Erreur lors de l'insertion de l'order", e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion pour saveAll Orders", e);
        }
        return savedOrders;
    }

}
