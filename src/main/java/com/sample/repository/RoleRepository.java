package com.sample.repository;

import com.sample.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link Role} entity.
 * Provides basic CRUD operations inherited from {@link JpaRepository}.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // Fetch Role by name
}
