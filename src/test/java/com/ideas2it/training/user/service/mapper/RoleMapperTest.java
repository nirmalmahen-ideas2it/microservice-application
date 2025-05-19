package com.ideas2it.training.user.service.mapper;

import com.ideas2it.training.user.domain.Role;
import com.ideas2it.training.user.dto.RoleCreateDto;
import com.ideas2it.training.user.dto.RoleInfo;
import com.ideas2it.training.user.dto.RoleUpdateDto;
import com.ideas2it.training.user.enums.RoleType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleMapperTest {

    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);

    @Test
    void testToEntity_FromRoleCreateDto() {
        // Arrange
        RoleCreateDto dto = RoleCreateDto
                .builder()
                .name(RoleType.ADMIN)
                .build();
        // Act
        Role role = roleMapper.toEntity(dto);

        // Assert
        assertNotNull(role);
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void testToEntity_FromRoleUpdateDto() {
        // Arrange
        RoleUpdateDto dto = RoleUpdateDto
                .builder()
                .name(RoleType.USER)
                .build();
        // Act
        Role role = roleMapper.toEntity(dto);

        // Assert
        assertNotNull(role);
        assertEquals("USER", role.getName());
    }

    @Test
    void testToInfo() {
        // Arrange
        Role role = new Role();
        role.setName("ADMIN");

        // Act
        RoleInfo roleInfo = roleMapper.toInfo(role);

        // Assert
        assertNotNull(roleInfo);
        assertEquals("ADMIN", roleInfo.getName());
    }

    @Test
    void testUpdateEntityFromDto() {
        // Arrange
        RoleUpdateDto dto = RoleUpdateDto
                .builder()
                .name(RoleType.USER)
                .build();
        Role role = new Role();
        role.setName(RoleType.ADMIN.name());

        // Act
        roleMapper.updateEntityFromDto(dto, role);

        // Assert
        assertEquals("USER", role.getName());
    }
}