package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserLoginRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserRegisterRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserUpdateRequestDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import org.mapstruct.*;

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

    @Mapping(target = "email", qualifiedByName = "obfuscateEmail")
    UserDTO toDTO(User user, @Context Boolean obfuscate);

    Set<UserDTO> toDTO(Set<User> users, @Context Boolean obfuscate);

    // Default implementation

    @Named("obfuscateEmail")
    default String obfuscateEmail(String email, @Context Boolean obfuscate) {
        if (obfuscate)
            return null;
        return email;
    }

}
