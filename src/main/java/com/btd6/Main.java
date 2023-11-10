package com.btd6;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class Main {
    public static void main(String[] args) {
        final Jdbi jdbi = Jdbi.create("jdbc:mariadb://localhost:3306/BTD6", "BTD6", "");
        jdbi.installPlugin(new SqlObjectPlugin());
        System.out.println("connecting");

        jdbi.useHandle(handle -> {
            //TODO: delete this and write tests
            ICustomerDAO customer = jdbi.onDemand(ICustomerDAO.class);
            customer.removeTable();
            customer.createTable();
            customer.truncate();
            customer.insert(new Customer("test firstname", "test lastname"));
            customer.getAll().forEach(System.out::println);
            Customer toUpdate = customer.getAll().get(0);
            toUpdate.setLastname("ödl");
            customer.update(toUpdate);
            customer.getAll().stream()
                .map(Customer::getLastname)
                .forEach(System.out::println);

            
        });
    }

}