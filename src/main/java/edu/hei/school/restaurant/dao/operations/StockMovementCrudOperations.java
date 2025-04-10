package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.PostgresNextReference;
import edu.hei.school.restaurant.dao.mapper.StockMovementMapper;
import edu.hei.school.restaurant.model.StockMovement;
import edu.hei.school.restaurant.service.exception.ServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.time.Instant.now;

@Repository
public class StockMovementCrudOperations implements CrudOperations<StockMovement> {
    final PostgresNextReference postgresNextReference = new PostgresNextReference();
    @Autowired
    private DataSource dataSource;
    @Autowired
    private StockMovementMapper stockMovementMapper;

    @Override
    public List<StockMovement> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StockMovement findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StockMovement> saveAll(List<StockMovement> entities) {
        List<StockMovement> stockMovements = new ArrayList<>();
        String sql = """
                insert into stock_movement (id, quantity, unit, movement_type, creation_datetime, id_ingredient)
                values (?, ?, cast(? as unit) , cast(? as  stock_movement_type), ?, ?)
                on conflict (id) do nothing returning id, quantity, unit, movement_type, creation_datetime, id_ingredient""";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            entities.forEach(entityToSave -> {
                try {
                    long id = entityToSave.getId() == null ? postgresNextReference.nextID("stock_movement", connection) : entityToSave.getId();
                    statement.setLong(1, id);
                    statement.setDouble(2, entityToSave.getQuantity());
                    statement.setString(3, entityToSave.getUnit().name());
                    statement.setString(4, entityToSave.getMovementType().name());
                    statement.setTimestamp(5, Timestamp.from(now()));
                    statement.setLong(6, entityToSave.getIngredient().getId());
                    statement.addBatch(); // group by batch so executed as one query in database
                } catch (SQLException e) {
                    throw new ServerException(e);
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(stockMovementMapper.apply(resultSet));
                }
            }
            return stockMovements;
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    public List<StockMovement> findByIdIngredient(Long idIngredient) {
        List<StockMovement> stockMovements = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select s.id, s.quantity, s.unit, s.movement_type, s.creation_datetime from stock_movement s"
                             + " join ingredient i on s.id_ingredient = i.id"
                             + " where s.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stockMovements.add(stockMovementMapper.apply(resultSet));
                }
                return stockMovements;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
