package org.fantasymaps.backend.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.fantasymaps.backend.model.Subscription;
import org.fantasymaps.backend.model.user.Creator;
import org.fantasymaps.backend.model.user.Customer;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
    @Column(name = "date_created")
    @NotNull(message = "Date is mandatory")
    private LocalDate dateCreated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creator_id")
    @NotNull(message = "Creator is mandatory")
    private Creator creator;

    @ManyToMany(mappedBy = "products")
    private Set<Subscription> subscriptions = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "boughtProducts")
    private Set<Customer> boughtCustomers = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "favoredProducts")
    private Set<Customer> favoredCustomers = new LinkedHashSet<>();

    public void prePersist() {
        if (subscriptions != null)
            for (Subscription subscription : subscriptions) {
                if (!subscription.getCreator().equals(creator))
                    throw new IllegalArgumentException("Subscription can only consist of products from the same creator");
            }
    }
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}