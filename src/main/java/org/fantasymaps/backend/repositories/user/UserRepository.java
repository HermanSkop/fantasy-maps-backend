package org.fantasymaps.backend.repositories.user;

import org.fantasymaps.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}