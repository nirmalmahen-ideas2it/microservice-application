package com.ideas2it.training.user.repository;

import com.ideas2it.training.user.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ImportAutoConfiguration(exclude = {LiquibaseAutoConfiguration.class})
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.vault.enabled=false",
        "spring.cloud.consul.enabled=false"
})
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByName_ExistingRole() {
        // Arrange
        Role role = new Role();
        role.setName("ADMIN");
        role.setCreatedBy("SYSTEM");
        roleRepository.save(role);

        // Act
        Optional<Role> result = roleRepository.findByName("ADMIN");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getName());
    }

    @Test
    void testFindByName_NonExistingRole() {
        // Act
        Optional<Role> result = roleRepository.findByName("NON_EXISTENT");

        // Assert
        assertFalse(result.isPresent());
    }
}