package com.btd6;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

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
    @SqlUpdate("DROP TABLE customers;")
    void removeTable();
}
