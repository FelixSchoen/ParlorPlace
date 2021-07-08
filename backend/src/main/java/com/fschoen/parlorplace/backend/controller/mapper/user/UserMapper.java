package com.fschoen.parlorplace.backend.controller.mapper.user;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSigninRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "nickname", ignore = true)
    User toUser(UserSignupRequestDTO userSignupRequestDTO);

    @Mappings({
            @Mapping(target = "nickname", ignore = true),
            @Mapping(target = "email", ignore = true)
    })
    User toUser(UserSigninRequestDTO userSigninRequestDTO);

    //@Mapping(target = "username", source = "username")
    UserDTO toDTO(User user);

}
