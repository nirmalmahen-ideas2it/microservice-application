package com.sample.repository;

import com.sample.domain.Role;
import com.sample.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link User} entity.
 * Provides basic CRUD operations inherited from {@link JpaRepository}.
 *
 * @author Alagu Nirmal Mahendran
 * @created 2025-04-21
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
