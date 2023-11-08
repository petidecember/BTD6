package com.btd6;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface IReadingDAO extends IDAO<IReading>{
    @Override
    @SqlUpdate("CREATE TABLE readings (" +
            "UUID VARCHAR(255) NOT NULL," +
            "Comment VARCHAR(255)," +
            "Customer VARCHAR(255)," +
            "DateOfReading DATE," +
            "Metercount DOUBLE," +
            "MeterId VARCHAR(255)," +
            "Substitute TINYINT(1)," +
            "PRIMARY KEY (UUID)," +
            "CONSTRAINT fk_reading_customer FOREIGN KEY (Customer)" +
            "REFERENCES customers (UUID) ON DELETE SET NULL" +
            ")")
    void createTable();

    @Override
    @SqlUpdate("DROP TABLE readings;")
    void removeTable();
}
