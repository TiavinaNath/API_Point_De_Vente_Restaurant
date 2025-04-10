package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.mapper.OrderMapper;
import edu.hei.school.restaurant.model.Ingredient;
import edu.hei.school.restaurant.model.Order;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderCrudOperations {
    private final DataSource dataSource;
    private final OrderMapper orderMapper;

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
}
