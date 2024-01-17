package com.btd6;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadingFactory {

    private final Connection connection;

    public ReadingFactory(Connection connection) {
        this.connection = connection;
    }

    public void saveReading(Reading reading) throws SQLException {
        String sql = "INSERT INTO readings (Meterid,Comment) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reading.getMeterId());
            preparedStatment.setString(2, reading.getComment());
            preparedStatement.executeUpdate();
        }
    }
}

