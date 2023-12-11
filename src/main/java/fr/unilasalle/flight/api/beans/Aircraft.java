package fr.unilasalle.flight.api.beans;

import fr.unilasalle.flight.api.constants.RegexpUtils;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.enterprise.inject.Model;
import jakarta.validation.constraints.*;
import lombok.*;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "aircraft")
public class Aircraft extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "aircraft_seq", sequenceName = "aircraft_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aircraft_seq")
    private Long aircraftId;

    @NotBlank(message = "Aircraft company cannot be blank")
    @Pattern(regexp = RegexpUtils.NAME_PATTERN, message = "Invalid aircraft company name")
    @Column(nullable = false)
    private String company;

    @NotBlank(message = "Aircraft type cannot be blank")
    @Pattern(regexp = RegexpUtils.NAME_PATTERN, message = "Invalid aircraft type")
    @Column(nullable = false)
    private String type;

    @NotBlank(message = "Registration code required")
    @Size(min = 5, max = 7, message = "Registration code should be 5-7 characters")
    @Pattern(regexp = RegexpUtils.REG_CODE_PATTERN, message = "Invalid registration code")
    @Column(unique = true, nullable = false)
    private String regCode;

    @NotNull(message = "Specify aircraft capacity")
    @Min(value = 1, message = "Capacity should be at least 1")
    @Column(nullable = false)
    private Integer capacity;
}
