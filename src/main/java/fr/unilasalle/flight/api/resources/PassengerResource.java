package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Passenger;
import fr.unilasalle.flight.api.repositories.PassengerRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassengerResource extends GenericResource {

    @Inject
    PassengerRepository repository;

    @Inject
    Validator validator;

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Passenger passenger = repository.findByIdOptional(id).orElse(null);
        return getOr404(passenger);
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response modifyPassenger(@PathParam("id") Long id, Passenger passenger) {
        Passenger existingPassenger = repository.findByIdOptional(id).orElse(null);
        if (existingPassenger == null) {
            return Response.status(404).build();
        }

        Set<ConstraintViolation<Passenger>> violations = validator.validate(passenger);
        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        try {
            repository.update("surname = ?1, firstname = ?2, emailAddress = ?3 where id = ?4",
                    passenger.getSurname(), passenger.getFirstname(), passenger.getEmailAddress(), id);
            return Response.ok(passenger).status(200).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }

    }
}
