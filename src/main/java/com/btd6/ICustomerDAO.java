package com.btd6;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

public interface ICustomerDAO extends IDAO<ICustomer>{
    @Override
    @SqlUpdate("CREATE TABLE customers (" +
            "UUID VARCHAR(255) NOT NULL," +
            "Firstname VARCHAR(255)," +
            "Lastname VARCHAR(255)," +
            "PRIMARY KEY (UUID)" +
            ")")
    void createTable();

    @Override
    @SqlUpdate("DROP TABLE customers")
    void removeTable();

    @Override
    @SqlUpdate("INSERT INTO customers (UUID, Firstname, Lastname) VALUES (:o.getUuid(), :o.getFirstname(), :o.getLastname())")
    boolean insert(ICustomer o);

    @Override
    @SqlQuery("SELECT * FROM customers WHERE UUID = :id")
    ICustomer findById(UUID id);

    @Override
    @SqlQuery("SELECT * FROM customers")
    List<? extends ICustomer> getAll();

    @Override
    @SqlUpdate("UPDATE customers SET Firstname = :o.getFirstname(), Lastname = :o.getLastname() WHERE UUID = :o.getUuid()")
    boolean update(ICustomer o);

    @Override
    @SqlUpdate("DELETE FROM customers WHERE UUID = :uuid")
    boolean delete(UUID uuid);

    @Override
    @SqlUpdate("TRUNCATE TABLE customers")
    void truncate();
}
