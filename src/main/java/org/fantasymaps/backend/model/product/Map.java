package org.fantasymaps.backend.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.fantasymaps.backend.config.AppConfig;
import org.fantasymaps.backend.model.Tag;

import java.util.Set;

@Getter
@Setter
@Entity
public class Map extends Product {
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = AppConfig.namePattern, message = AppConfig.nameMismatchMessage)
    private String name;
    @Embedded
    private MapSize size;
    @Column(name = "description")
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @Column(name = "map_image_url")
    @NotBlank(message = "Map image URL cannot be blank")
    @Pattern(regexp = AppConfig.urlPattern, message = AppConfig.urlMismatchMessage)
    private String mapUrl;
}