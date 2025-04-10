package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextReference;
import edu.hei.school.restaurant.dao.mapper.DishIngredientMapper;
import edu.hei.school.restaurant.model.DishIngredient;
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

import static java.sql.Types.OTHER;

@Repository
@RequiredArgsConstructor
public class DishIngredientCrudOperations implements CrudOperations<DishIngredient>{
    private final DataSource dataSource;
    private final DishIngredientMapper dishIngredientMapper;
    final PostgresNextReference postgresNextReference = new PostgresNextReference();

    @Override
    public List<DishIngredient> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public DishIngredient findById(Long id) {
        return null;
    }

    @Override
    public List<DishIngredient> saveAll(List<DishIngredient> entities) {
        return List.of();
    }

    @SneakyThrows
    public List<DishIngredient> saveAll(List<DishIngredient> entities, Long idDish) {
        List<DishIngredient> savedDishIngredients = new ArrayList<>();

        String sql = """
            INSERT INTO dish_ingredient (id, id_dish, id_ingredient, required_quantity, unit)
            VALUES (?, ?, ?, ?, CAST(? AS unit))
            ON CONFLICT (id) DO NOTHING
            RETURNING id, id_dish, id_ingredient, required_quantity, unit
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (DishIngredient entity : entities) {
                try {
                    long id = entity.getId() == null
                            ? postgresNextReference.nextID("dish_ingredient", connection)
                            : entity.getId();

                    statement.setLong(1, id);
                    statement.setLong(2, idDish);
                    statement.setLong(3, entity.getIngredient().getId());
                    statement.setDouble(4, entity.getRequiredQuantity());
                    statement.setString(5, entity.getUnit().name());
                    statement.addBatch();

                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    savedDishIngredients.add(dishIngredientMapper.apply(resultSet));
                }
            }

            return savedDishIngredients;

        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }


    public List<DishIngredient> findByIdDish(Long idDish) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("""
                    select di.id, di.id_dish, di.id_ingredient, di.required_quantity, di.unit from dish_ingredient di 
                    where di.id_dish = ?
             """)) {
            statement.setLong(1, idDish);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DishIngredient dishIngredient = dishIngredientMapper.apply(resultSet);
                    dishIngredients.add(dishIngredient);
                }
                return dishIngredients;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}

