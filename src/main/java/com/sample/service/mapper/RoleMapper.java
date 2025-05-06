package com.sample.service.mapper;

import com.sample.domain.Role;
import com.sample.dto.RoleCreateDto;
import com.sample.dto.RoleInfo;
import com.sample.dto.RoleUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for converting between Role entities and DTOs.
 * <p>
 * This interface provides methods to map between Role, RoleCreateDto, RoleUpdateDto, and RoleInfo.
 * It also includes logic for updating existing Role entities.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    /**
     * Maps a RoleCreateDto to a Role entity.
     *
     * @param dto the RoleCreateDto containing role creation details
     * @return the mapped Role entity
     */
    Role toEntity(RoleCreateDto dto);

    /**
     * Maps a RoleUpdateDto to a Role entity.
     *
     * @param dto the RoleUpdateDto containing role update details
     * @return the mapped Role entity
     */
    Role toEntity(RoleUpdateDto dto);

    /**
     * Maps a Role entity to a RoleInfo DTO.
     *
     * @param role the Role entity
     * @return the mapped RoleInfo DTO
     */
    RoleInfo toInfo(Role role);

    /**
     * Updates an existing Role entity with values from a RoleUpdateDto.
     * Null values in the DTO are ignored during the update.
     *
     * @param dto    the RoleUpdateDto containing updated role details
     * @param entity the existing Role entity to be updated
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RoleUpdateDto dto, @MappingTarget Role entity);
}
