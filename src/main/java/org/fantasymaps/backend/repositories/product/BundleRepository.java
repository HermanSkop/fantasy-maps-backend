package org.fantasymaps.backend.repositories.product;

import org.fantasymaps.backend.model.product.Bundle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BundleRepository extends JpaRepository<Bundle, Integer> {
    @EntityGraph(attributePaths = {"maps"})
    Set<Bundle> findByCreatorId(Integer id);
}
