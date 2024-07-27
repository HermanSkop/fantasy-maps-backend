package org.fantasymaps.backend.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;

@Getter
@Setter
@Entity
public class Bundle extends Product {
    @Column(name = "name")
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = AppConfig.namePattern, message = AppConfig.nameMismatchMessage)
    private String name;
}