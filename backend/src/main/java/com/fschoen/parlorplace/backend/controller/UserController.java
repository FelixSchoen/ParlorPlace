package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.controller.mapper.user.UserMapper;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.repository.RoleRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.security.JwtUtils;
import com.fschoen.parlorplace.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService, RoleRepository roleRepository, UserMapper userMapper, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signupUser(@Valid @RequestBody UserSignupRequestDTO userSignupRequestDTO) {
        User proposedUser = userMapper.toUser(userSignupRequestDTO);
        User createdUser = userService.signup(proposedUser);
        UserDTO userDTO = userMapper.toDTO(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testMethod() {
        return ResponseEntity.status(HttpStatus.OK).body("Das ist ein Test");
    }

}
