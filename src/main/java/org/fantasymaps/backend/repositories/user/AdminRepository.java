package org.fantasymaps.backend.repositories.user;

import org.fantasymaps.backend.model.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}