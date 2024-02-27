package com.btd6;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Path("api")
public class ReadingResource {
    private ICustomerDAO customerDAO;
    private IReadingDAO readingDAO;
    private URI uri;

    @POST
    @Path("readings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(@Valid RReading readingDto) {
        if(customerDAO.findById(readingDto.getCustomer().getUuid()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Reading toBeInserted = RestTransformer.restReadingToDomainRandomUUID(readingDto);
        readingDAO.insert(toBeInserted);
        return Response.created(uri).entity(readingDAO.findById(toBeInserted.getUuid())).build();
    }

    @GET
    @Path("readings/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReading(@PathParam("uuid") UUID uuid) {
        Reading reading = readingDAO.findById(uuid);
        if(reading != null) {
            return Response.ok().entity(reading).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("readings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadingsInTimePeriod(@QueryParam("customer") UUID customer,
                                            @QueryParam("start") Optional<LocalDate> start,
                                            @QueryParam("end") Optional<LocalDate> end) {
        if(customerDAO.findById(customer) != null) {
            List<Reading> readings = readingDAO.getAll();
            RReadings readingsDto = new RReadings(readings.stream()
                    .filter(r -> (r.getCustomer().getUuid().equals(customer)
                            && start.map(localDate -> r.getDateOfReading().isAfter(localDate)).orElse(true)
                            && end.map(date -> r.getDateOfReading().isBefore(date)).orElse(true)))
                    .map(RestTransformer::domainReadingToRest)
                    .toList());
            return Response.ok().entity(readingsDto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("readings")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReading(@Valid RReading readingDto) {
        Reading toBeUpdated = RestTransformer.restReadingToDomain(readingDto);
        if(readingDAO.update(toBeUpdated)) {
            return Response.ok(String.format("Reading with id %s has been updated", toBeUpdated.getUuid())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("readings/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReading(@PathParam("uuid") UUID uuid) {
        Reading reading = readingDAO.findById(uuid);
        if(reading != null) {
            RReading readingDto = RestTransformer.domainReadingToRest(reading);
            customerDAO.delete(reading.getUuid());
            return Response.ok().entity(readingDto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
