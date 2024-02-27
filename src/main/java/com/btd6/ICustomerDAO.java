package com.btd6;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

public interface ICustomerDAO extends IDAO<Customer>{
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
    @SqlUpdate("INSERT INTO customers (UUID, Firstname, Lastname) VALUES (:uuid, :firstname, :lastname)")
    boolean insert(@BindBean Customer o);

    @Override
    @SqlQuery("SELECT * FROM customers WHERE UUID = :uuid")
    @RegisterConstructorMapper(Customer.class)
    Customer findById(@Bind("uuid") UUID uuid);

    @Override
    @SqlQuery("SELECT * FROM customers")
    @RegisterConstructorMapper(Customer.class)
    List<Customer> getAll();

    @Override
    @SqlUpdate("UPDATE customers SET Firstname = :firstname, Lastname = :lastname WHERE UUID = :uuid")
    boolean update(@BindBean Customer o);

    @Override
    @SqlUpdate("DELETE FROM customers WHERE UUID = :uuid")
    boolean delete(@Bind("uuid") UUID uuid);

    @Override
    @SqlUpdate("TRUNCATE TABLE customers")
    void truncate();
}
