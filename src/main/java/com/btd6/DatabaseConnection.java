package com.btd6;


import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DatabaseConnection implements IConnection {
    private IReadingDAO readings;
    private ICustomerDAO customers;

    private Handle handle;


    @Override
    public IConnection openConnection(String url, String user, String password) {
        final Jdbi jdbi = Jdbi.create(url, user, password);
        jdbi.installPlugin(new SqlObjectPlugin());
        System.out.println("connecting");

        handle = jdbi.open();
        customers = handle.attach(ICustomerDAO.class);
        readings = handle.attach(IReadingDAO.class);
        return this;
    }

    @Override
    public IFacilityService getFacilityService() {
        return null;
    }

    @Override
    public ICustomerDAO getCustomerDAO() {
        return this.customers;
    }

    @Override
    public IReadingDAO getReadingDAO() {
        return this.readings;
    }

    @Override
    public void createAllTables() {
        this.customers.createTable();
        this.readings.createTable();
    }

    @Override
    public void removeAllTables() {
        this.readings.removeTable();
        this.customers.removeTable();
    }

    @Override
    public void truncateAllTables() {
        this.readings.truncate();
        this.customers.truncate();
    }

    @Override
    public void closeConnection() {
        handle.close();
    }
}
