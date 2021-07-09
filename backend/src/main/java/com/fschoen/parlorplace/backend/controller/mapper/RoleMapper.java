package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    Role toRole(String role);

    Set<Role> toRole(Set<String> role);

    default UserRole toUserRole(Role role) {
        return role.getRole();
    };

    Set<UserRole> toUserRole(Set<Role> roles);

}
