package fr.unilasalle.flight.api.beans;

import fr.unilasalle.flight.api.constants.RegexpUtils;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "planes")
public class Plane extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "planes_sequence", sequenceName = "planes_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planes_sequence")
    private Long id;

    @NotBlank(message = "Enter Aircraft Operator")
    @Pattern(regexp = RegexpUtils.ALPHA_REGEXP, message = "Operator Name format invalid")
    @Column(nullable = false)
    private String operator;

    @NotBlank(message = "Enter Aircraft Model")
    @Pattern(regexp = RegexpUtils.ALPHA_REGEXP, message = "Model Name format invalid")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Enter Aircraft Registration")
    @Size(min = 6, max = 6, message = "Registration should be 6 characters")
    @Pattern(regexp = RegexpUtils.UPPERCASE_ALPHA_REGEXP, message = "Registration format invalid")
    @Column(unique = true, nullable = false)
    private String registration;

    @NotNull(message = "Specify Aircraft Capacity")
    @Min(value = 2, message = "Minimum Capacity should be 2")
    @Column(nullable = false)
    private Long capacity;
}
