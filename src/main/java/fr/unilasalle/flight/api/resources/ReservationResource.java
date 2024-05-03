package fr.unilasalle.flight.api.resources;

import fr.unilasalle.flight.api.beans.Flight;
import fr.unilasalle.flight.api.beans.Passenger;
import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.repositories.FlightRepository;
import fr.unilasalle.flight.api.repositories.PassengerRepository;
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
import org.eclipse.microprofile.openapi.annotations.Operation;


import java.util.List;
import java.util.Set;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource extends GenericResource {

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    FlightRepository flightRepository;

    @Inject
    PassengerRepository passengerRepository;

    @Inject
    Validator validator;

    @GET
    public Response getReservations(@QueryParam("flightNumber") String flightNumber) {
        List<Reservation> reservations;

        if (StringUtils.isNotBlank(flightNumber)) {
            reservations = reservationRepository.findByFlightNumber(flightNumber);
        } else {
            reservations = reservationRepository.listAll();
        }

        return Response.ok(reservations).status(200).build();
    }

    @POST
    @Transactional
    @Operation(summary = "create a reservation")
    public Response createReservation(Reservation reservation) {
        Set<ConstraintViolation<Reservation>> reservationViolation = validator.validate(reservation);
        if (!reservationViolation.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(reservationViolation)).build();
        }

        Set<ConstraintViolation<Passenger>> passengerViolations = validator.validate(reservation.getPassenger());
        if (!passengerViolations.isEmpty()) {
            return Response.status(400).entity(new ErrorWrapper(passengerViolations)).build();
        }

        Flight flight;
        if (StringUtils.isNotBlank(reservation.getFlight().getNumber())) {
            flight = flightRepository.findByNumber(reservation.getFlight().getNumber());
            if (flight == null) {
                return Response.status(400).entity(new ErrorWrapper("Flight does not exist")).build();
            }
            if(reservationRepository.countByFlightNumber(reservation.getFlight().getNumber()).equals(flight.getPlane().getCapacity())){
                return Response.status(400).entity(new ErrorWrapper("Flight is complete")).build();
            }
            reservation.setFlight(flight);
        }

        Passenger passenger = passengerRepository.findByEmail(reservation.getPassenger().getEmailAddress());
        if (passenger == null) {
            try {
                passengerRepository.persistAndFlush(reservation.getPassenger());
                passenger = passengerRepository.findByEmail(reservation.getPassenger().getEmailAddress());
            } catch (PersistenceException ex) {
                return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
            }
        }
        reservation.setPassenger(passenger);

        try {
            reservationRepository.persistAndFlush(reservation);
            return Response.ok(reservation).status(201).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }


    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteReservation(@PathParam("id") Long id) {
        Reservation reservation = reservationRepository.findByIdOptional(id).orElse(null);
        if (reservation == null) {
            return Response.status(404).build();
        }

        try {
            reservationRepository.deleteById(id);
            return Response.ok().status(200).build();
        } catch (PersistenceException ex) {
            return Response.serverError().entity(new ErrorWrapper(ex.getMessage())).build();
        }
    }
}
