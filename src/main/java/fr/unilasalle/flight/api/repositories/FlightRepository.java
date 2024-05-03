package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Flight;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class FlightRepository implements PanacheRepositoryBase<Flight, Long> {

    public List<Flight> findByDestination(String destination) {
        return find("destination", destination).stream().toList();
    }

    public Flight findByNumber(String number) {
        return find("number", number).firstResultOptional().orElse(null);
    }
}
