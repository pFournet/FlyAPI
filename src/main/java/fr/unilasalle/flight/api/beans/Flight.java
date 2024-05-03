package fr.unilasalle.flight.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unilasalle.flight.api.constants.RegexpUtils;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "flights_sequence", sequenceName = "flights_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flights_sequence")
    private Long id;

    @NotBlank(message = "Provide Flight Number")
    @Size(min = 6, max = 6, message = "Flight Number should be 6 characters")
    @Pattern(regexp = RegexpUtils.UPPERCASE_ALPHA_REGEXP, message = "Invalid Flight Number format")
    @Column(unique = true, nullable = false)
    private String number;

    @NotBlank(message = "Enter Flight Origin")
    @Column(nullable = false)
    private String origin;

    @NotBlank(message = "Enter Flight Destination")
    @Column(nullable = false)
    private String destination;

    @NotNull(message = "Specify Departure Date")
    @Column(nullable = false)
    private LocalDate departureDate;

    @NotNull(message = "Specify Departure Time")
    @Column(nullable = false)
    private LocalTime departureTime;

    @NotNull(message = "Specify Arrival Date")
    @Column(nullable = false)
    private LocalDate arrivalDate;

    @NotNull(message = "Specify Arrival Time")
    @Column(nullable = false)
    private LocalTime arrivalTime;

    @NotNull(message = "Specify Plane for the Flight")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "planeId", referencedColumnName = "id", nullable = false)
    private Plane plane;

    @JsonIgnore
    @OneToMany(targetEntity = Reservation.class, mappedBy = "flight")
    private List<Reservation> reservations = new ArrayList<>();

}
