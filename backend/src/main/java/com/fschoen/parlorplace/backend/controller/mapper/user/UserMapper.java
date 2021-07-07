package com.fschoen.parlorplace.backend.controller.mapper.user;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "nickname", ignore = true)
    User toUser(UserSignupRequestDTO userSignupRequestDTO);

    //@Mapping(target = "username", source = "username")
    UserDTO toDTO(User user);

}
