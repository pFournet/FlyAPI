package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Aircraft;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;

public class AircraftRepository implements PanacheRepositoryBase<Aircraft, Long> {

    public List<Aircraft> findByCompany(String company) {
        return find("company", company).list();
    }

    public Aircraft findByRegCode(String regCode) {
        return find("regCode", regCode).firstResultOptional().orElse(null);
    }
}
