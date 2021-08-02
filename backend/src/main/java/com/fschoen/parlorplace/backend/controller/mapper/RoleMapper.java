package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import org.mapstruct.*;

import java.util.*;

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
    }

    Set<UserRole> toUserRole(Set<Role> roles);

}
