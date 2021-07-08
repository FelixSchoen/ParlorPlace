package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSigninRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSigninResponseDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.controller.mapper.user.UserMapper;
import com.fschoen.parlorplace.backend.entity.POJO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.validation.Validator;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signupUser(@RequestBody @Valid UserSignupRequestDTO userSignupRequestDTO, BindingResult bindingResult) {
        System.out.println(userSignupRequestDTO);
        if (bindingResult.hasErrors()) {
            System.out.println("Error");
        }
        User proposedUser = userMapper.toUser(userSignupRequestDTO);
        User createdUser = userService.signup(proposedUser);
        UserDTO userDTO = userMapper.toDTO(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserSigninResponseDTO> signinUser(@Valid @RequestBody UserSigninRequestDTO userSigninRequestDTO) {
        UserSigninResponseDTO userSigninResponseDTO = userService.signin(userMapper.toUser(userSigninRequestDTO));

        return ResponseEntity.status(HttpStatus.OK).body(userSigninResponseDTO);
    }

}
