package com.ideas2it.training.jhipster.userrole.app.service.mapper;

import com.ideas2it.training.jhipster.userrole.app.domain.Role;
import com.ideas2it.training.jhipster.userrole.app.dto.RoleCreateDto;
import com.ideas2it.training.jhipster.userrole.app.dto.RoleInfo;
import com.ideas2it.training.jhipster.userrole.app.dto.RoleUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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
