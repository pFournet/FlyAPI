package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Plane;
import fr.unilasalle.flight.api.repositories.PlaneRepository;
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

@Path("/planes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlaneResource extends GenericResource {

    @Inject
    PlaneRepository repository;

    @Inject
    Validator validator;

    @GET
    public Response getPlanes(@QueryParam("operator") String operator) {
        List<Plane> planes;
        if (StringUtils.isBlank(operator)) {
            planes = repository.listAll();
        } else {
            planes = repository.findByOperator(operator);
        }
        return getOr404(planes);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Plane plane = repository.findByIdOptional(id).orElse(null);
        return getOr404(plane);
    }

    @POST
    @Transactional
    public Response createPlane(Plane plane) {
        Set<ConstraintViolation<Plane>> violations = validator.validate(plane);
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        try {
            repository.persistAndFlush(plane);
            return Response.ok(plane).status(201).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}
