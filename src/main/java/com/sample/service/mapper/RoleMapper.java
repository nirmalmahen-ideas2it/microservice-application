package com.sample.service.mapper;

import com.sample.domain.Role;
import com.sample.service.dto.RoleCreateDto;
import com.sample.service.dto.RoleUpdateDto;
import com.sample.service.dto.RoleInfo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // Map RoleCreateDto to Role
    Role toEntity(RoleCreateDto dto);

    // Map RoleUpdateDto to Role
    Role toEntity(RoleUpdateDto dto);

    // Map Role to RoleInfo
    RoleInfo toInfo(Role role);

    // Update existing Role entity with RoleUpdateDto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RoleUpdateDto dto, @MappingTarget Role entity);
}
