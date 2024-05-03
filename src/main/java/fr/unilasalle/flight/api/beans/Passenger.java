package fr.unilasalle.flight.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.unilasalle.flight.api.constants.RegexpUtils;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "passengers")
public class Passenger extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "passengers_sequence", sequenceName = "passengers_sequence", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passengers_sequence")
    private Long id;

    @NotBlank(message = "Provide Passenger Last Name")
    @Pattern(regexp = RegexpUtils.ALPHA_REGEXP, message = "Invalid Last Name format")
    @Column(nullable = false)
    private String surname;

    @NotBlank(message = "Provide Passenger First Name")
    @Pattern(regexp = RegexpUtils.ALPHA_REGEXP, message = "Invalid First Name format")
    @Column(nullable = false)
    private String firstname;

    @NotBlank(message = "Email Address required")
    @Pattern(regexp = RegexpUtils.EMAIL_REGEXP, message = "Email Address format incorrect")
    @Size(max = 150, message = "Email Address should be under 150 characters")
    @Column(unique = true, nullable = false)
    private String emailAddress;

    @JsonIgnore
    @OneToMany(targetEntity = Reservation.class, mappedBy = "passenger")
    private List<Reservation> reservations = new ArrayList<>();
}