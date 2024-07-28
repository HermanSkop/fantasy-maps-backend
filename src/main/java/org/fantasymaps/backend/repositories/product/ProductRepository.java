package org.fantasymaps.backend.repositories.product;

import org.fantasymaps.backend.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}