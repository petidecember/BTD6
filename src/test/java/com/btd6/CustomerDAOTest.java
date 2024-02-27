package com.btd6;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerDAOTest {
    final static Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test", "", "");
    ICustomerDAO customers;
    Handle handle;

    @BeforeAll
    public static void init() {
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    @BeforeEach
    public void setup() {
        handle = jdbi.open();
        customers = handle.attach(ICustomerDAO.class);
        customers.createTable();
        customers.insert(new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Max", "Mustermann"));
        customers.insert(new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"), "Peter", "Parker"));
        customers.insert(new Customer(UUID.fromString("edb85564-9f0c-4502-9294-c0e86293d1d3"), "Harry", "Potter"));
    }
    @AfterEach
    public void undo() {
        handle.close();
    }

    @Test
    public void randomUUIDTest() {
        Customer expected = new Customer("Larry", "Holster");
        customers.insert(expected);
        Customer customer = customers.getAll().stream().filter(expected::equals).findFirst().get();
        assertEquals(customer.getUuid(), expected.getUuid());
    }

    @Test
    public void fieldTest() {
        Customer customer = customers.getAll().get(0);
        Customer expected = new Customer("Max", "Mustermann");
        assertEquals(customer.getFirstname(), expected.getFirstname());
        assertEquals(customer.getLastname(), expected.getLastname());
    }

    @Test
    public void findById() {
        Customer expected = new Customer(UUID.fromString("edb85564-9f0c-4502-9294-c0e86293d1d3"), "Harry", "Potter");
        assertEquals(expected, customers.findById(UUID.fromString(("edb85564-9f0c-4502-9294-c0e86293d1d3"))));
    }

    @Test
    public void throwExceptionOnSameInsertUUID() {
        assertThrows(UnableToExecuteStatementException.class,
                () -> {
                    customers.insert(new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Max", "Mustermann"));
                });
    }

    @Test
    public void update() {
        Customer expected = new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"), "asd", "test");
        customers.update(expected);
        assertEquals(customers.getAll().get(1), expected);
    }

    @Test
    public void insert() {
        Customer expected = new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"), "asd", "test");
        customers.insert(expected);
        assertEquals(customers.getAll().get(3), expected);
    }

    @Test
    public void delete() {
        customers.delete(UUID.fromString("edb85564-9f0c-4502-9294-c0e86293d1d3"));
        assertNull(customers.findById(UUID.fromString("edb85564-9f0c-4502-9294-c0e86293d1d3")));
    }

    @Test
    public void truncate() {
        customers.truncate();
        assertArrayEquals(customers.getAll().toArray(), new List[]{});
    }
}
