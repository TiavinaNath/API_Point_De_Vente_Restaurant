package edu.hei.school.restaurant.dao;

import edu.hei.school.restaurant.service.exception.ServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgresNextReference  {

    public long nextID(String tableName, Connection connection) throws SQLException {
        final String columnName = "id";

        String sequenceQuery = "SELECT pg_get_serial_sequence('" + tableName + "', '" + columnName + "')";
        try (PreparedStatement stmt = connection.prepareStatement(sequenceQuery);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String sequenceName = rs.getString(1);
                String nextvalQuery = "SELECT nextval('" + sequenceName + "')";
                try (PreparedStatement nextValStmt = connection.prepareStatement(nextvalQuery);
                     ResultSet nextValRs = nextValStmt.executeQuery()) {
                    if (nextValRs.next()) {
                        return nextValRs.getLong(1);
                    }
                }
            }
        }
        throw new ServerException("Unable to find sequence for table " + tableName);
    }
}
