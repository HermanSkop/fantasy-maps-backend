package org.fantasymaps.backend.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.fantasymaps.backend.config.AppConfig;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Bundle extends Product {
    @Column(name = "name")
    @NotBlank(message = "Name cannot be blank")
    @Pattern(regexp = AppConfig.namePattern, message = AppConfig.nameMismatchMessage)
    private String name;

    @ManyToMany
    @JoinTable(name = "bundle_maps",
            joinColumns = @JoinColumn(name = "bundle_id"),
            inverseJoinColumns = @JoinColumn(name = "maps_id"))
    private Set<Map> maps = new LinkedHashSet<>();

    @Override
    @PrePersist
    public void prePersist() {
        super.prePersist();
        if (this.getMaps() == null || this.getMaps().isEmpty())
            throw new IllegalArgumentException("Bundle must contain at least one map");
        if (this.getMaps().contains(null))
            throw new IllegalArgumentException("Bundle cannot contain null maps");
    }
}