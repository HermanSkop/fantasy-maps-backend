package org.fantasymaps.backend.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @ElementCollection
    //@CollectionTable(name = "permissions", joinColumns = @JoinColumn(name = "admin_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private List<Permission> permissions;
}