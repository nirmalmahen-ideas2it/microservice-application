package com.ideas2it.training.user.repository;

import com.ideas2it.training.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
