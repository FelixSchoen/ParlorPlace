package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserLoginRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserRegisterRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserUpdateRequestDTO;
import com.fschoen.parlorplace.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    User fromDTO(UserDTO userDTO);

    User fromDTO(UserRegisterRequestDTO userRegisterRequestDTO);

    @Mappings({
            @Mapping(target = "nickname", ignore = true),
            @Mapping(target = "email", ignore = true)
    })
    User fromDTO(UserLoginRequestDTO userLoginRequestDTO);

    User fromDTO(UserUpdateRequestDTO userUpdateRequestDTO);

    //

    @Mapping(target = "userRoles", source = "roles")
    UserDTO toDTO(User user);

    Set<UserDTO> toDTO(Set<User> users);

}
