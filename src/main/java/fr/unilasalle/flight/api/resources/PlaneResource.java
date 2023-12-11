package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Aircraft;
import fr.unilasalle.flight.api.repositories.AircraftRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

@Path("/aircrafts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AircraftResource extends GenericResource {

    @Inject
    AircraftRepository repository;

    @Inject
    Validator validator;

    @GET
    public Response getAircrafts(@QueryParam("company") String company) {
        List<Aircraft> aircrafts;
        if (StringUtils.isBlank(company)) {
            aircrafts = repository.listAll();
        } else {
            aircrafts = repository.findByCompany(company);
        }
        return getOr404(aircrafts);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Aircraft aircraft = repository.findByIdOptional(id).orElse(null);
        return getOr404(aircraft);
    }

    @POST
    @Transactional
    public Response createAircraft(Aircraft aircraft) {
        Set<ConstraintViolation<Aircraft>> violations = validator.validate(aircraft);
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        try {
            repository.persistAndFlush(aircraft);
            return Response.ok(aircraft).status(201).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}
