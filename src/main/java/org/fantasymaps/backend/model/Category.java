package org.fantasymaps.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false)
    @Pattern(regexp = AppConfig.namePattern, message = AppConfig.nameMismatchMessage)
    @NotBlank(message = "Name cannot be blank")
    private String name;
}