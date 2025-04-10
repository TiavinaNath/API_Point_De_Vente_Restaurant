package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextReference;
import edu.hei.school.restaurant.dao.mapper.DishMapper;
import edu.hei.school.restaurant.model.Dish;
import edu.hei.school.restaurant.service.exception.NotFoundException;
import edu.hei.school.restaurant.service.exception.ServerException;
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
public class DishCrudOperations implements CrudOperations<Dish>{
    private final DataSource dataSource;
    private final DishMapper dishMapper;
    private final DishIngredientCrudOperations dishIngredientCrudOperations;

    final PostgresNextReference postgresNextReference = new PostgresNextReference();

    @Override
    public List<Dish> getAll(int page, int size) {
        List<Dish> dishes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select d.id, d.name, d.price from dish d order by d.id asc limit ? offset ?")) {
            statement.setInt(1, size);
            statement.setInt(2, size * (page - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Dish dish = dishMapper.apply(resultSet);
                    dishes.add(dish);
                }
                return dishes;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public Dish findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select d.id, d.name, d.price from dish d where d.id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return dishMapper.apply(resultSet);
                }
                throw new NotFoundException("Dish.id=" + id + " not found");
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    @SneakyThrows
    public List<Dish> saveAll(List<Dish> entities) {
        List<Dish> dishes = new ArrayList<>();
        String sql = """
            INSERT INTO dish (id, name, price)
            VALUES (?, ?, ?)
            ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, price = EXCLUDED.price
            RETURNING id, name, price
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Dish entityToSave : entities) {
                try {
                    long id = entityToSave.getId() == null
                            ? postgresNextReference.nextID("dish", connection)
                            : entityToSave.getId();

                    statement.setLong(1, id);
                    statement.setString(2, entityToSave.getName());
                    statement.setDouble(3, entityToSave.getPrice());
                    statement.addBatch();

                    dishIngredientCrudOperations.saveAll(entityToSave.getDishIngredients(), id);

                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    dishes.add(dishMapper.apply(resultSet));
                }
            }

            return dishes;

        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }


}
