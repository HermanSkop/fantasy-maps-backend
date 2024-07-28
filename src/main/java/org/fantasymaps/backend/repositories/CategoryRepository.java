package org.fantasymaps.backend.repositories;

import org.fantasymaps.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}