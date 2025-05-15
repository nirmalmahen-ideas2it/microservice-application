package com.sample.service.mapper;

import com.sample.domain.Role;
import com.sample.domain.User;
import com.sample.dto.UserCreateDto;
import com.sample.dto.UserInfo;
import com.sample.dto.UserUpdateDto;
import com.sample.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToEntity_FromUserCreateDto() {
        // Arrange
        UserCreateDto dto = UserCreateDto
                .builder()
                .username("testuser")
                .password("password")
                .build();

        // Act
        User user = userMapper.toEntity(dto);

        // Assert
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
    }

    @Test
    void testToEntity_FromUserUpdateDto() {
        // Arrange
        Set<RoleType> roleTypes = Set.of(RoleType.ADMIN, RoleType.USER);
        UserUpdateDto dto = UserUpdateDto
                .builder()
                .username("updateduser")
                .roles(roleTypes)
                .build();
        // Act
        User user = userMapper.toEntity(dto);

        // Assert
        assertNotNull(user);
        assertEquals("updateduser", user.getUsername());
        assertEquals(2, user.getRoles().size());
    }

    @Test
    void testToInfo() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName("ADMIN");
        roles.add(role);
        user.setRoles(roles);

        // Act
        UserInfo userInfo = userMapper.toInfo(user);

        // Assert
        assertNotNull(userInfo);
        assertEquals("testuser", userInfo.getUsername());
        assertTrue(userInfo.getRoles().contains("ADMIN"));
    }

    @Test
    void testUpdateEntityFromDto() {
        // Arrange
        UserUpdateDto dto = UserUpdateDto
                .builder()
                .username("updateduser")
                .build();
        User user = new User();
        user.setUsername("olduser");

        // Act
        userMapper.updateEntityFromDto(dto, user);

        // Assert
        assertEquals("updateduser", user.getUsername());
    }

    @Test
    void testMapRoles() {
        // Arrange
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setName("ADMIN");
        roles.add(role);

        // Act
        Set<String> roleNames = userMapper.mapRoles(roles);

        // Assert
        assertNotNull(roleNames);
        assertTrue(roleNames.contains("ADMIN"));
    }

    @Test
    void testMapRoleTypesToRoles() {
        // Arrange
        Set<RoleType> roleTypes = Set.of(RoleType.ADMIN, RoleType.USER);

        // Act
        Set<Role> roles = userMapper.mapRoleTypesToRoles(roleTypes);

        // Assert
        assertNotNull(roles);
        assertEquals(2, roles.size());
    }
}