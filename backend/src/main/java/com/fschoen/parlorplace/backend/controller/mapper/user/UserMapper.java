package com.fschoen.parlorplace.backend.controller.mapper.user;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSigninRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserUpdateRequestDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    User toUser(UserSignupRequestDTO userSignupRequestDTO);

    @Mappings({
            @Mapping(target = "nickname", ignore = true),
            @Mapping(target = "email", ignore = true)
    })
    User toUser(UserSigninRequestDTO userSigninRequestDTO);

    @Mapping(target = "roles", source = "roles")
    User toUser(UserUpdateRequestDTO userUpdateRequestDTO);

    UserDTO toDTO(User user);

}
