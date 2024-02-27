package com.btd6;

import org.jdbi.v3.sqlobject.config.RegisterArgumentFactory;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

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
    @SqlUpdate("INSERT INTO readings (UUID, Comment, Customer, DateOfReading, Metercount, MeterId, Substitute) VALUES (:uuid, :comment, :customer, :dateOfReading, :metercount, :meterId, :substitute)")
    @RegisterArgumentFactory(CustomerAsArgumentFactory.class)
    boolean insert(@BindBean Reading o);

    @Override
    @SqlQuery("SELECT r.*, c.UUID as c_uuid, c.firstname as c_firstname, c.lastname as c_lastname FROM readings AS r INNER JOIN customers AS c ON Customer = c.UUID WHERE r.UUID = :uuid")
    @RegisterConstructorMapper(Reading.class)
    Reading findById(@Bind("uuid") UUID uuid);

    @Override
    @SqlQuery("SELECT r.*, c.UUID as c_uuid, c.firstname as c_firstname, c.lastname as c_lastname FROM readings AS r INNER JOIN customers AS c ON Customer = c.UUID")
    @RegisterConstructorMapper(Reading.class)
    List<Reading> getAll();

    @Override
    @SqlUpdate("UPDATE readings SET Comment = :comment, Customer = :customer, DateOfReading = :dateOfReading, Metercount = :metercount, MeterId = :meterId, Substitute = :substitute WHERE UUID = :uuid")
    @RegisterArgumentFactory(CustomerAsArgumentFactory.class)
    boolean update(@BindBean Reading o);

    @Override
    @SqlUpdate("DELETE FROM readings WHERE UUID = :uuid")
    boolean delete(@Bind("uuid") UUID uuid);

    @Override
    @SqlUpdate("TRUNCATE TABLE readings")
    void truncate();
}
