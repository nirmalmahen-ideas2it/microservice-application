package com.ideas2it.training.user.service.mapper;

import com.ideas2it.training.user.domain.Role;
import com.ideas2it.training.user.domain.User;
import com.ideas2it.training.user.dto.UserCreateDto;
import com.ideas2it.training.user.dto.UserInfo;
import com.ideas2it.training.user.dto.UserUpdateDto;
import com.ideas2it.training.user.enums.RoleType;
import org.mapstruct.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for converting between User entities and DTOs.
 * <p>
 * This interface provides methods to map between User, UserCreateDto, UserUpdateDto, and UserInfo.
 * It also includes custom mapping logic for roles.
 * </p>
 *
 * @author Alagu Nirmal Mahendran
 * @since 05-06-2025
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a UserCreateDto to a User entity.
     *
     * @param dto the UserCreateDto containing user creation details
     * @return the mapped User entity
     */
    User toEntity(UserCreateDto dto);

    /**
     * Maps a UserUpdateDto to a User entity.
     * Custom mapping is applied to handle roles.
     *
     * @param dto the UserUpdateDto containing user update details
     * @return the mapped User entity
     */
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleTypesToRoles")
    User toEntity(UserUpdateDto dto);

    /**
     * Maps a User entity to a UserInfo DTO.
     *
     * @param user the User entity
     * @return the mapped UserInfo DTO
     */
    @Mapping(target = "roles", source = "roles")
    UserInfo toInfo(User user);

    /**
     * Updates an existing User entity with values from a UserUpdateDto.
     * Null values in the DTO are ignored during the update.
     *
     * @param dto    the UserUpdateDto containing updated user details
     * @param entity the existing User entity to be updated
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdateDto dto, @MappingTarget User entity);

    /**
     * Maps a set of Role entities to a set of role names (strings).
     *
     * @param roles the set of Role entities
     * @return a set of role names
     */
    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream()
                .map(Role::getName) // Assuming Role has a getName() method
                .collect(Collectors.toSet());
    }

    /**
     * Maps a set of RoleType enums to a set of Role entities.
     * This is a custom mapping method used for role conversion.
     *
     * @param roleTypes the set of RoleType enums
     * @return a set of Role entities
     */
    @Named("mapRoleTypesToRoles")
    default Set<Role> mapRoleTypesToRoles(Set<RoleType> roleTypes) {
        if (roleTypes == null || roleTypes.isEmpty()) {
            return new HashSet<>();
        }
        return roleTypes.stream()
                .map(roleType -> {
                    Role role = new Role();
                    role.setName(roleType.name());
                    return role;
                })
                .collect(Collectors.toSet());
    }
}
