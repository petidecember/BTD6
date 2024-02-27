package com.btd6;

import jakarta.ws.rs.core.UriBuilder;
import lombok.Getter;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Server extends ResourceConfig {
    @Getter
    private final URI uri;

    private final IConnection databaseConnection;

    private org.eclipse.jetty.server.Server jettyServer;

    public Server(String url, IConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        uri = UriBuilder.fromUri(url).build();
        databaseConnection.createAllTables();
        register(new CustomerResource(databaseConnection.getCustomerDAO(), databaseConnection.getReadingDAO(), getUri()));
        register(new ReadingResource(databaseConnection.getCustomerDAO(), databaseConnection.getReadingDAO(), getUri()));
        register(LocalDateParamConverterProvider.class);
    }

    public org.eclipse.jetty.server.Server startServer() {
        this.jettyServer = JettyHttpContainerFactory.createServer(uri, this);
        return this.jettyServer;
    }

    public void stopServer() {
        databaseConnection.removeAllTables();
        try {
            this.jettyServer.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
