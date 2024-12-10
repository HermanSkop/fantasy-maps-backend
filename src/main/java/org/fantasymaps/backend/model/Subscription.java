package org.fantasymaps.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.fantasymaps.backend.config.AppConfig;
import org.fantasymaps.backend.model.product.Product;
import org.fantasymaps.backend.model.user.Creator;
import org.fantasymaps.backend.model.user.Customer;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", length = AppConfig.nameMaxLength)
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = AppConfig.namePattern, message = AppConfig.nameMismatchMessage)
    private String name;
    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
    @Column(name = "description")
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Column(name = "date_created")
    @NotNull(message = "Date is mandatory")
    private LocalDate date;

    @ManyToMany
    @JoinTable(name = "subscription_products",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "products_id"))
    private Set<Product> products = new LinkedHashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    @NotNull(message = "Creator is mandatory")
    private Creator creator;

    @ManyToMany(mappedBy = "subscriptions")
    private Set<Customer> subscribedCustomers = new LinkedHashSet<>();

}