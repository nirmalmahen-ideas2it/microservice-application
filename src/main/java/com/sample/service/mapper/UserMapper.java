package com.sample.service.mapper;

import com.sample.domain.Role;
import com.sample.domain.User;
import com.sample.enums.RoleType;
import com.sample.service.dto.UserCreateDto;
import com.sample.service.dto.UserInfo;
import com.sample.service.dto.UserUpdateDto;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface UserMapper {

    // Map userCreateDto to user
    User toEntity(UserCreateDto dto);

    // Map userUpdateDto to user
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleTypesToRoles")
    User toEntity(UserUpdateDto dto);

    // Map user to userInfo
    @Mapping(target = "roles", source = "roles")
    UserInfo toInfo(User user);

    // Update existing user entity with userUpdateDto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdateDto dto, @MappingTarget User entity);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
            .map(Role::getName) // Assuming Role has a getName() method
            .collect(Collectors.toSet());
    }

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
