package org.fantasymaps.backend.repositories.user;

import org.fantasymaps.backend.model.user.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, Integer> {
}