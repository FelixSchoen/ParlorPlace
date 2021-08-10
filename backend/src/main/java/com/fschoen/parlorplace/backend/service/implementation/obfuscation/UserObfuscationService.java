package com.fschoen.parlorplace.backend.service.implementation.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.ObfuscationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserObfuscationService extends ObfuscationService<UserDTO> {

    @Autowired
    public UserObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public UserDTO obfuscateFor(UserDTO userDTO, User user) {
        if (userDTO.getId().equals(user.getId()))
            return userDTO;
        else
            return userDTO.toBuilder().email(null).build();
    }

}
