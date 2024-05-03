package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Passenger;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

@Model
public class PassengerRepository implements PanacheRepositoryBase<Passenger, Long> {

    public Passenger findByEmail(String emailAddress) {
        return find("emailAddress", emailAddress).firstResultOptional().orElse(null);
    }
}
