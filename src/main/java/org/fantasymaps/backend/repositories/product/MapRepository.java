package org.fantasymaps.backend.repositories.product;

import org.fantasymaps.backend.model.product.Map;
import org.fantasymaps.backend.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MapRepository extends JpaRepository<Map, Integer> {
    Set<Map> findByCreator_Id(Integer id);
}