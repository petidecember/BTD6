package com.btd6;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface IReadingDAO extends IDAO<Reading>{
    @Override
    @SqlUpdate("CREATE TABLE readings (" +
            "UUID VARCHAR(255) NOT NULL," +
            "Comment VARCHAR(255)," +
            "Customer VARCHAR(255)," +
            "DateOfReading DATE," +
            "Metercount DOUBLE," +
            "MeterId VARCHAR(255)," +
            "Substitute BOOLEAN," +
            "PRIMARY KEY (UUID)," +
            "CONSTRAINT fk_reading_customer FOREIGN KEY (Customer)" +
            "REFERENCES customers (UUID) ON DELETE SET NULL" +
            ")")
    void createTable();

    @Override
    @SqlUpdate("DROP TABLE readings")
    void removeTable();

    @Override
    @SqlUpdate("INSERT INTO readings (UUID, Comment, Customer, DateOfReading, Metercount, MeterId, Substitute) VALUES (:uuid, :comment, :customer, :dateofreading, :metercount, :meterid, :substitute)")
    boolean insert(@BindBean Reading o);

    @Override
    @SqlQuery("SELECT * FROM readings WHERE UUID = :id")
    @RegisterConstructorMapper(Reading.class)
    Reading findById(String id);

    @Override
    @SqlQuery("SELECT * FROM readings")
    @RegisterConstructorMapper(Reading.class)
    List<Reading> getAll();

    @Override
    @SqlUpdate("UPDATE readings SET Comment = :comment, Customer = :customer, DateOfReading = :dateofreading, Metercount = :metercount, MeterId = :meterid, Susbtitute = :substitute WHERE UUID = :uuid")
    boolean update(@BindBean Reading o);

    @Override
    @SqlUpdate("DELETE FROM readings WHERE UUID = :uuid")
    boolean delete(String uuid);

    @Override
    @SqlUpdate("TRUNCATE TABLE readings")
    void truncate();
}
