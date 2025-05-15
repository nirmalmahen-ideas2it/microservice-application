package com.sample.service.mapper;

import com.sample.domain.Role;
import com.sample.dto.RoleCreateDto;
import com.sample.dto.RoleInfo;
import com.sample.dto.RoleUpdateDto;
import com.sample.enums.RoleType;
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