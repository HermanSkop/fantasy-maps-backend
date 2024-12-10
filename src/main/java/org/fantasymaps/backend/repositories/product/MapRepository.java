package org.fantasymaps.backend.repositories.product;

import org.fantasymaps.backend.model.product.Map;
import org.fantasymaps.backend.model.user.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MapRepository extends JpaRepository<Map, Integer> {
    @EntityGraph(attributePaths = {"tags"})
    Set<Map> findByCreatorId(Integer id);

    Set<Map> findAllByCreatorId(int creatorId);

    Set<Map> findAllByIdIn(Set<Integer> ids);

    Set<Map> findAllByFavoredCustomers(Set<Customer> favoredCustomers);
}