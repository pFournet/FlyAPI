package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Flight;
import fr.unilasalle.flight.api.beans.Plane;
import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.repositories.FlightRepository;
import fr.unilasalle.flight.api.repositories.PlaneRepository;
import fr.unilasalle.flight.api.repositories.ReservationRepository;
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

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource extends GenericResource {

    @Inject
    FlightRepository flightRepository;

    @Inject
    PlaneRepository planeRepository;

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    Validator validator;

    @GET
    public Response getFlights(@QueryParam("destination") String destination) {
        List<Flight> flights;
        if (StringUtils.isBlank(destination)) {
            flights = flightRepository.listAll();
        } else {
            flights = flightRepository.findByDestination(destination);
        }
        return getOr404(flights);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Flight flight = flightRepository.findByIdOptional(id).orElse(null);
        return getOr404(flight);
    }

    @POST
    @Transactional
    public Response createFlight(Flight flight) {
        Set<ConstraintViolation<Flight>> violations = validator.validate(flight);

        if (!violations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(violations)).build();
        }

        Plane plane;
        if (StringUtils.isNotBlank(flight.getPlane().getRegistration())) {
            plane = planeRepository.findByRegistration(flight.getPlane().getRegistration());
            if (plane == null) {
                return Response.status(400).entity(new ErrorWrapper("Provided Plane does not exist")).build();
            }
            flight.setPlane(plane);
        }

        try {
            flightRepository.persistAndFlush(flight);
            return Response.ok(flight).status(201).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteFlight(@PathParam("id") Long id) {
        Flight flight = flightRepository.findByIdOptional(id).orElse(null);
        if (flight == null) {
            return Response.status(404).build();
        }

        List<Reservation> reservations = reservationRepository.findByFlightId(id);
        for (Reservation reservation : reservations) {
            try {
                reservationRepository.deleteById(reservation.getId());
            } catch (PersistenceException ex) {
                return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
            }
        }

        try {
            flightRepository.deleteById(id);
            return Response.ok().status(200).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}
