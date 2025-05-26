package com.ideas2it.training.user.repository;

import com.ideas2it.training.user.domain.Role;
import com.ideas2it.training.user.domain.User;
import com.ideas2it.training.user.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ImportAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    void testFindByUsername_ExistingUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser_1");
        user.setPassword("password");
        userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findByUsername("testuser_1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("testuser_1", result.get().getUsername());
    }

    @Test
    void testFindByUsername_NonExistingUser() {
        // Act
        Optional<User> result = userRepository.findByUsername("nonexistent");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindById_ExistingUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        User savedUser = userRepository.save(user);

        // Act
        Optional<User> result = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(savedUser.getId(), result.get().getId());
    }

    @Test
    void testFindById_NonExistingUser() {
        // Act
        Optional<User> result = userRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveUser_WithRoles() {
        // Arrange
        Role role = new Role();
        role.setName(RoleType.USER.name());
        roleRepository.save(role);

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Set.of(role));

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals(1, savedUser.getRoles().size());
        assertEquals(RoleType.USER.name(), savedUser.getRoles().iterator().next().getName());
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        User savedUser = userRepository.save(user);

        // Act
        userRepository.deleteById(savedUser.getId());
        Optional<User> result = userRepository.findById(savedUser.getId());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveUser_NullFields() {
        // Arrange
        User user = new User();
        user.setUsername(null);
        user.setPassword(null);

        // Act & Assert
        assertThrows(Exception.class, () -> userRepository.save(user));
    }

}