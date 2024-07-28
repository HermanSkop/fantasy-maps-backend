package org.fantasymaps.backend.repositories.user;

import org.fantasymaps.backend.model.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}