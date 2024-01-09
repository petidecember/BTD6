package com.btd6;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReadingDAOTest {
    final static Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test", "", "");
    IReadingDAO readings;
    ICustomerDAO customers;

    Handle handle;

    Customer customer = new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Filip", "Marzec");

    @BeforeAll
    public static void init() {
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    @BeforeEach
    public void setup() {
        handle = jdbi.open();
        customers = handle.attach(ICustomerDAO.class);
        customers.createTable();
        readings = handle.attach(IReadingDAO.class);
        readings.createTable();
        readings.insert(new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Störrdem", customer, LocalDate.of(1806, 1, 1), 2000.0, "test meterid", false));
        readings.insert(new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"), "Erdem", customer, LocalDate.of(2012, 12, 12), 30.0, "test id", true));
        readings.insert(new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"), "Öhrdem", customer, LocalDate.of(2002, 12, 29 ), 23.0, "Test meterid1", true));
    }
    @AfterEach
    public void undo() {
        handle.close();
    }

    @Test
    public void meterId() {
        Reading reading1 = readings.getAll().get(0);
        assertEquals(reading1.getMeterId(), "test meterid");
    }

    @Test
    public void findById() {
        Reading expected = new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"), "Erdem", customer, LocalDate.of(2012, 12, 12), 30.0, "test id", true);
        assertEquals(expected, readings.findById("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"));
    }

    @Test
    public void throwExceptionOnSameInsertUUID() {
        assertThrows(UnableToExecuteStatementException.class,
                () -> {
                    readings.insert(new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"), "Erdem", customer, LocalDate.of(2012, 12, 12), 30.0, "test id", true));

                });
    }

    @Test
    public void update() {
        Reading expected = new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"), "Erdem", customer, LocalDate.of(2012, 12, 12), 50.0, "test id", false);
        readings.update(expected);
        assertEquals(readings.getAll().get(1), expected);
    }

    @Test
    public void insert() {
        Reading expected = new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaad"), "Test Comment", customer, LocalDate.of(2022, 6, 9 ), 43.0, "meterid3", true);
        readings.insert(expected);
        assertEquals(readings.getAll().get(3), expected);
    }

    @Test
    public void delete() {
        readings.delete("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
        assertNull(readings.findById("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"));
    }

    @Test
    public void truncate() {
        readings.truncate();
        assertArrayEquals(readings.getAll().toArray(), new List[]{});
    }
}
