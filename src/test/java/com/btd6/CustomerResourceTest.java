package com.btd6;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerResourceTest {
    Server server;
    WebTarget target;
    DatabaseConnection databaseConnection;

    @BeforeEach
    public void setup() {
        databaseConnection = (DatabaseConnection) new DatabaseConnection().openConnection("jdbc:h2:mem:test", "", "");
        server = new Server("http://localhost:8080/", databaseConnection);
        server.startServer();
        Client client = ClientBuilder.newClient();
        target = client.target(server.getUri());
    }

    @AfterEach
    public void teardown() {
        server.stopServer();
    }

    @Test
    public void create() {
        RCustomer expected = new RCustomer("Max", "Muster");
        RCustomer got = target.path("/api/customers")
                .request()
                .post(Entity.json(expected))
                .readEntity(RCustomer.class);
        assertEquals(expected.getFirstname(), got.getFirstname());
        assertEquals(expected.getLastname(), got.getLastname());
    }

    @Test
    public void createWithInvalidJSON() {
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                target.path("/api/customers")
                        .request()
                        .post(Entity.json("{}"))
                        .getStatus());
    }

    @Test
    public void readAll() {
        Customer customer1 = new Customer("Max", "Muster");
        Customer customer2 = new Customer("Mox", "Muster");
        Customer customer3 = new Customer("Mux", "Muster");
        databaseConnection.getCustomerDAO().insert(customer1);
        databaseConnection.getCustomerDAO().insert(customer2);
        databaseConnection.getCustomerDAO().insert(customer3);
        assertEquals(Stream.of(customer1, customer2, customer3)
                        .map(RestTransformer::domainCustomerToRest)
                        .toList(),
                target.path("/api/customers")
                        .request()
                        .get(RCustomers.class)
                        .getCustomers());
    }

    @Test
    public void read() {
        Customer customer = new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Max", "Muster");
        databaseConnection.getCustomerDAO().insert(customer);

        assertEquals(RestTransformer.domainCustomerToRest(customer),
                target.path("/api/customers/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .request()
                        .get(RCustomer.class));
    }

    @Test
    public void readInvalidUUID() {
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                target.path("/api/customers/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .request()
                        .get()
                        .getStatus());
    }

    @Test
    public void update() {
        Customer customer = new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Max", "Muster");
        databaseConnection.getCustomerDAO().insert(customer);
        RCustomer withUpdate = new RCustomer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Max", "Mustermann");

        String message = target.path("/api/customers")
                .request()
                .put(Entity.json(withUpdate))
                .readEntity(String.class);

        assertEquals(String.format("Customer with id %s has been updated", withUpdate.getUuid()), message);

        RCustomer updatedDto = target.path("/api/customers/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .request()
                .get(RCustomer.class);

        assertEquals(withUpdate, updatedDto);
    }

    @Test
    public void updateInvalidUUID() {
        RCustomer withUpdate = new RCustomer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Max", "Mustermann");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                target.path("/api/customers")
                        .request()
                        .put(Entity.json(withUpdate))
                        .getStatus());
    }

    @Test
    public void updateWithInvalidJSON() {
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),
                target.path("/api/customers")
                        .request()
                        .put(Entity.json("{}"))
                        .getStatus());
    }

    @Test
    public void delete() {
        Customer customer = new Customer(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Max", "Muster");
        databaseConnection.getCustomerDAO().insert(customer);
        Reading reading = new Reading(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "Comment", customer, LocalDate.of(2024, 1, 1), 2000.0, "test meterid", false);
        databaseConnection.getReadingDAO().insert(reading);

        CustomerWithReadingsDTO dto = target.path("/api/customers/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .request()
                .delete()
                .readEntity(CustomerWithReadingsDTO.class);

        assertEquals(RestTransformer.domainCustomerToRest(customer), dto.getCustomer());
        assertEquals(List.of(RestTransformer.domainReadingToRest(reading)), dto.getReadings());
    }

    @Test
    public void deleteInvalidUUID() {
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),
                target.path("/api/customers/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .request()
                        .delete()
                        .getStatus());
    }
}
