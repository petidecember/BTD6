package com.btd6;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadingResourceTest {
    Server server;
    WebTarget target;
    DatabaseConnection databaseConnection;

    Customer customer = new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Max", "Muster");

    @BeforeEach
    public void setup() {
        databaseConnection = (DatabaseConnection) new DatabaseConnection().openConnection("jdbc:h2:mem:test", "", "");
        server = new Server("http://localhost:8080/", databaseConnection);
        server.startServer();
        Client client = ClientBuilder.newClient();
        target = client.target(server.getUri());
        databaseConnection.getCustomerDAO().insert(customer);
    }

    @AfterEach
    public void teardown() {
        server.stopServer();
    }

    @Test
    public void create() {
        Reading expected = new Reading("Comment", customer, LocalDate.of(2000, 1, 1), 2000.0, "test meterid", false);
        RReading got = target.path("/api/readings")
                .request()
                .post(Entity.json(expected))
                .readEntity(RReading.class);
        assertEquals(expected.getComment(), got.getComment());
        assertEquals(expected.getCustomer(), RestTransformer.restCustomerToDomain(got.getCustomer()));
        assertEquals(expected.getDateOfReading(), got.getDateOfReading());
        assertEquals(expected.getMetercount(), got.getMetercount());
        assertEquals(expected.getMeterId(), got.getMeterId());
        assertEquals(expected.getSubstitute(), got.getSubstitute());
    }

    @Test
    public void createWithInvalidJSON() {
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                target.path("/api/readings")
                        .request()
                        .post(Entity.json("{}"))
                        .getStatus());
    }

    @Test
    public void read() {
        Reading reading = new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Comment", customer, LocalDate.of(2000, 1, 1), 2000.0, "test meterid", false);
        databaseConnection.getReadingDAO().insert(reading);

        assertEquals(RestTransformer.domainReadingToRest(reading),
                target.path("/api/readings/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .request()
                        .get(RReading.class));
    }

    @Test
    public void readInvalidUUID() {
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                target.path("/api/readings/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .request()
                        .get()
                        .getStatus());
    }

    @Nested
    class ReadWithTimePeriods {
        Reading reading1;
        Reading reading2;
        Reading reading3;

        @BeforeEach
        public void setup() {
            reading1 = new Reading("Comment", customer, LocalDate.of(1980, 1, 1), 2000.0, "test meterid", false);
            reading2 = new Reading("Comment", customer, LocalDate.of(1990, 1, 1), 2000.0, "test meterid", false);
            reading3 = new Reading("Comment", customer, LocalDate.of(2000, 1, 1), 2000.0, "test meterid", false);
            databaseConnection.getReadingDAO().insert(reading1);
            databaseConnection.getReadingDAO().insert(reading2);
            databaseConnection.getReadingDAO().insert(reading3);
        }

        @Test
        public void readWithNoTimePeriod() {
            assertEquals(Stream.of(reading1, reading2, reading3)
                            .map(RestTransformer::domainReadingToRest)
                            .toList(),
                    target.path("/api/readings")
                            .queryParam("customer", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                            .request()
                            .get(RReadings.class)
                            .getReadings());
        }

        @Test
        public void readWithinTimePeriod() {
            List<RReading> dto = target.path("/api/readings")
                    .queryParam("customer", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                    .queryParam("start", "1979-01-01")
                    .queryParam("end", "1981-01-01")
                    .request()
                    .get()
                    .readEntity(RReadings.class)
                    .getReadings();
            assertEquals(RestTransformer.domainReadingToRest(reading1),
                    dto.get(0));
            assertEquals(1,
                    dto.size());
        }

        @Test
        public void readWithEndPeriodOnly() {
            List<RReading> dto = target.path("/api/readings")
                    .queryParam("customer", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                    .queryParam("end", "1991-01-01")
                    .request()
                    .get()
                    .readEntity(RReadings.class)
                    .getReadings();
            assertEquals(RestTransformer.domainReadingToRest(reading1),
                    dto.get(0));
            assertEquals(RestTransformer.domainReadingToRest(reading2),
                    dto.get(1));
            assertEquals(2,
                    dto.size());
        }

        @Test
        public void readWithStartPeriodOnly() {
            List<RReading> dto = target.path("/api/readings")
                    .queryParam("customer", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                    .queryParam("start", "1981-01-01")
                    .request()
                    .get()
                    .readEntity(RReadings.class)
                    .getReadings();
            assertEquals(RestTransformer.domainReadingToRest(reading2),
                    dto.get(0));
            assertEquals(RestTransformer.domainReadingToRest(reading3),
                    dto.get(1));
            assertEquals(2,
                    dto.size());
        }

        @Test
        public void readWithInvalidDate() {
            assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                    target.path("/api/readings")
                            .queryParam("customer", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                            .queryParam("start", "01/01/1981")
                            .request()
                            .get()
                            .getStatus());
        }
    }

    @Test
    public void update() {
        Reading reading = new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Comment", customer, LocalDate.of(2000, 1, 1), 2000.0, "test meterid", false);
        databaseConnection.getReadingDAO().insert(reading);
        RReading withUpdate = new RReading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Comment", RestTransformer.domainCustomerToRest(customer), LocalDate.of(2000, 1, 1), 1950.0, "test meterid", false);

        String message = target.path("/api/readings")
                .request()
                .put(Entity.json(withUpdate))
                .readEntity(String.class);

        assertEquals(String.format("Reading with id %s has been updated", withUpdate.getUuid()), message);

        RReading updatedDto = target.path("/api/readings/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .request()
                .get(RReading.class);

        assertEquals(withUpdate, updatedDto);
    }

    @Test
    public void updateInvalidUUID() {
        RReading withUpdate = new RReading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Comment", RestTransformer.domainCustomerToRest(customer), LocalDate.of(2000, 1, 1), 1950.0, "test meterid", false);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                target.path("/api/readings")
                        .request()
                        .put(Entity.json(withUpdate))
                        .getStatus());
    }

    @Test
    public void updateWithInvalidJSON() {
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                target.path("/api/readings")
                        .request()
                        .put(Entity.json("{}"))
                        .getStatus());
    }

    @Test
    public void delete() {
        Reading reading = new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Comment", customer, LocalDate.of(2024, 1, 1), 2000.0, "test meterid", false);
        databaseConnection.getReadingDAO().insert(reading);

        RReading dto = target.path("/api/readings/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .request()
                .delete()
                .readEntity(RReading.class);

        assertEquals(RestTransformer.domainReadingToRest(reading), dto);
    }

    @Test
    public void deleteInvalidUUID() {
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                target.path("/api/readings/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .request()
                        .delete()
                        .getStatus());
    }
}
