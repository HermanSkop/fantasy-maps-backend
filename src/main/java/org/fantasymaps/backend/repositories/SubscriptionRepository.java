package org.fantasymaps.backend.repositories;

import org.fantasymaps.backend.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
}