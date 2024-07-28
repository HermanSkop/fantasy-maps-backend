package org.fantasymaps.backend.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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
public class Customer extends User {
    @ManyToMany
    @JoinTable(name = "customer_subscriptions",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriptions_id"))
    private Set<Subscription> subscriptions = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "bought_customer_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "products_id"))
    private Set<Product> boughtProducts = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "favored_customer_products",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "products_id"))
    private Set<Product> favoredProducts = new LinkedHashSet<>();

}