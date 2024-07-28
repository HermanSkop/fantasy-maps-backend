package org.fantasymaps.backend.repositories.product;

import org.fantasymaps.backend.model.product.Bundle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BundleRepository extends JpaRepository<Bundle, Integer> {
}