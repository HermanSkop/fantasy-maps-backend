package org.fantasymaps.backend.model.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.fantasymaps.backend.config.AppConfig;
import org.fantasymaps.backend.model.Subscription;
import org.fantasymaps.backend.model.product.Product;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Creator extends User {
    @Column(name = "avatar")
    @Pattern(regexp = AppConfig.urlPattern, message = AppConfig.urlMismatchMessage)
    private String avatarUrl;
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "creator", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Subscription> subscriptions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Product> products = new LinkedHashSet<>();

}