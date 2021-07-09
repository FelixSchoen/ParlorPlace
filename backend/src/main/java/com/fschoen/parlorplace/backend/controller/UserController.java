package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.user.*;
import com.fschoen.parlorplace.backend.controller.mapper.user.UserMapper;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.service.UserService;
import com.fschoen.parlorplace.backend.validation.implementation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserValidator validator = new UserValidator();;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signupUser(@RequestBody UserSignupRequestDTO userSignupRequestDTO) {
        validator.validate(userSignupRequestDTO).throwIfInvalid();

        User proposedUser = userMapper.toUser(userSignupRequestDTO);
        User createdUser = userService.signup(proposedUser);
        UserDTO userDTO = userMapper.toDTO(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserSigninResponseDTO> signinUser(@RequestBody UserSigninRequestDTO userSigninRequestDTO) {
        validator.validate(userSigninRequestDTO).throwIfInvalid();

        UserSigninResponseDTO userSigninResponseDTO = userService.signin(userMapper.toUser(userSigninRequestDTO));

        return ResponseEntity.status(HttpStatus.OK).body(userSigninResponseDTO);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        validator.validate(userUpdateRequestDTO).throwIfInvalid();

        User proposedUser = userMapper.toUser(userUpdateRequestDTO);
        User updatedUser = userService.update(proposedUser);
        UserDTO userDTO = userMapper.toDTO(updatedUser);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

}
