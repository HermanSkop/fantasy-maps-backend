package org.fantasymaps.backend.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.fantasymaps.backend.config.AppConfig;
import org.fantasymaps.backend.model.Category;
import org.fantasymaps.backend.model.Tag;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Map extends Product {
    @Column(name = "name")
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

    @ManyToMany
    @JoinTable(name = "map_tags",
            joinColumns = @JoinColumn(name = "map_id"),
            inverseJoinColumns = @JoinColumn(name = "tags_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @ManyToMany
    @NotEmpty(message = "Map must have at least one category")
    @JoinTable(name = "map_categories",
            joinColumns = @JoinColumn(name = "map_id"),
            inverseJoinColumns = @JoinColumn(name = "categories_id"))
    private Set<Category> categories = new LinkedHashSet<>();
}