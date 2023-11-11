package com.btd6;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        final Jdbi jdbi = Jdbi.create("jdbc:mariadb://localhost:3306/BTD6", "BTD6", "");
        jdbi.installPlugin(new SqlObjectPlugin());
        System.out.println("connecting");

        jdbi.useHandle(handle -> {
            //TODO: delete this and write tests
            ICustomerDAO customer = jdbi.onDemand(ICustomerDAO.class);
            Customer customerToUpdate = customer.getAll().get(0);
            {
                customer.removeTable();
                customer.createTable();
                customer.truncate();
                customer.insert(new Customer("test firstname", "test lastname"));
                customer.getAll().forEach(System.out::println);
                customerToUpdate.setLastname("ödl");
                customer.update(customerToUpdate);
                customer.getAll().stream()
                        .map(Customer::getLastname)
                        .forEach(System.out::println);
            }

            IReadingDAO readings = jdbi.onDemand(IReadingDAO.class);
            {
                //readings.removeTable();
                readings.createTable();
                readings.insert(new Reading("test comment", customerToUpdate, LocalDate.now(), 2365.336, "test meterid", false));
                //readings.findById();
                readings.getAll().forEach(System.out::println);
                Reading readingToUpdate = readings.getAll().get(0);
                readingToUpdate.setComment("fuck öttl");
                readings.update(readingToUpdate);
                readings.getAll().stream()
                        .map(Reading::getComment)
                        .forEach(System.out::println);

            }

            
        });
    }

}