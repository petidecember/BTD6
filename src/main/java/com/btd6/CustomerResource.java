package com.btd6;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Path("api")
public class CustomerResource {
    private ICustomerDAO customerDAO;
    private IReadingDAO readingDAO;
    private URI uri;

    @POST
    @Path("customers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(@Valid RCustomer customerDto) {
        Customer toBeInserted = RestTransformer.restCustomerToDomainRandomUUID(customerDto);
        customerDAO.insert(toBeInserted);
        return Response.created(uri).entity(customerDAO.findById(toBeInserted.getUuid())).build();
    }

    @GET
    @Path("customers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomers() {
        RCustomers customerDtos = new RCustomers(customerDAO
                .getAll()
                .stream()
                .map(RestTransformer::domainCustomerToRest)
                .toList());
        return Response.ok().entity(customerDtos).build();
    }

    @GET
    @Path("customers/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("uuid") UUID uuid) {
        Customer customer = customerDAO.findById(uuid);
        if(customer != null) {
            return Response.ok().entity(customer).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("customers")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@Valid RCustomer customerDto) {
        Customer toBeUpdated = RestTransformer.restCustomerToDomain(customerDto);
        if(customerDAO.update(toBeUpdated)) {
            return Response.ok(String.format("Customer with id %s has been updated", toBeUpdated.getUuid())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("customers/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("uuid") UUID uuid) {
        Customer customer = customerDAO.findById(uuid);
        if(customer != null) {
            RCustomer customerDto = RestTransformer.domainCustomerToRest(customer);
            List<RReading> readingsDto = readingDAO.getAll().
                    stream()
                    .filter(r -> r.getCustomer().equals(customer))
                    .map(RestTransformer::domainReadingToRest)
                    .toList();
            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JSR310Module());
            String response;
            try {
                response = om.writeValueAsString(new CustomerWithReadingsDTO(customerDto, readingsDto));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            customerDAO.delete(customer.getUuid());
            return Response.ok().entity(response).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
