package org.fantasymaps.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = AppConfig.namePattern, message = AppConfig.nameMismatchMessage)
    private String name;
    @Column(name = "price", nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
    @Column(name = "description", nullable = false)
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Column(name = "date_created", nullable = false)
    private LocalDate date;
}