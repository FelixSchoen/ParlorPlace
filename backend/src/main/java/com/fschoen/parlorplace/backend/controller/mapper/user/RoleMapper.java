package com.fschoen.parlorplace.backend.controller.mapper.user;

import com.fschoen.parlorplace.backend.entity.persistance.Role;
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

}
