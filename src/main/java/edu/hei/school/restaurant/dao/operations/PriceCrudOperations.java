package edu.hei.school.restaurant.dao.operations;

import edu.hei.school.restaurant.dao.DataSource;
import edu.hei.school.restaurant.dao.mapper.PriceMapper;
import edu.hei.school.restaurant.dao.PostgresNextReference;
import edu.hei.school.restaurant.model.Price;
import edu.hei.school.restaurant.service.exception.ServerException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PriceCrudOperations implements CrudOperations<Price> {
    final PostgresNextReference postgresNextReference = new PostgresNextReference();
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PriceMapper priceMapper;

    @Override
    public List<Price> getAll(int page, int size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Price findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SneakyThrows
    @Override
    public List<Price> saveAll(List<Price> entities) {
        List<Price> prices = new ArrayList<>();
        String sql = """
            INSERT INTO price (
                id, amount, date_value, id_ingredient
            )
            VALUES (?, ?, ?, ?)
            ON CONFLICT (id) DO NOTHING
            RETURNING id, amount, date_value, id_ingredient
            """;

        try (Connection connection = dataSource.getConnection()) {
            for (Price entityToSave : entities) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    long id = entityToSave.getId() == null
                            ? postgresNextReference.nextID("price", connection)
                            : entityToSave.getId();

                    statement.setLong(1, id);
                    statement.setDouble(2, entityToSave.getAmount());
                    statement.setDate(3, Date.valueOf(entityToSave.getDateValue()));
                    statement.setLong(4, entityToSave.getIngredient().getId());

                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            prices.add(priceMapper.apply(resultSet));
                        }
                    }
                }
            }
            return prices;
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }


    public List<Price> findByIdIngredient(Long idIngredient) {
        List<Price> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select p.id, p.amount, p.date_value from price p"
                     + " join ingredient i on p.id_ingredient = i.id"
                     + " where p.id_ingredient = ?")) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Price price = priceMapper.apply(resultSet);
                    prices.add(price);
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }
}
