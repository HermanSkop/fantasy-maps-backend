package org.fantasymaps.backend.repositories;

import org.fantasymaps.backend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
}