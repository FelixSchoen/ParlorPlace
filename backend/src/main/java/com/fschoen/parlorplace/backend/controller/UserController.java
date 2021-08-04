package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.authentication.TokenRefreshRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.authentication.TokenRefreshResponseDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserLoginRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserLoginResponseDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserRegisterRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserUpdateRequestDTO;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.service.ObfuscationService;
import com.fschoen.parlorplace.backend.service.UserService;
import com.fschoen.parlorplace.backend.service.implementation.obfuscation.UserObfuscationService;
import com.fschoen.parlorplace.backend.validation.implementation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final ObfuscationService<UserDTO> obfuscationService;

    private final UserMapper userMapper;

    private final UserValidator validator = new UserValidator();

    @Autowired
    public UserController(UserService userService, ObfuscationService<UserDTO> obfuscationService, UserMapper userMapper) {
        this.userService = userService;
        this.obfuscationService = obfuscationService;
        this.userMapper = userMapper;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        validator.validate(userRegisterRequestDTO).throwIfInvalid();

        User proposedUser = userMapper.fromDTO(userRegisterRequestDTO);
        User createdUser = userService.register(proposedUser);
        UserDTO userDTO = userMapper.toDTO(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        validator.validate(userLoginRequestDTO).throwIfInvalid();

        UserLoginResponseDTO userLoginResponseDTO = userService.login(userMapper.fromDTO(userLoginRequestDTO));

        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDTO> refreshUser(@RequestBody TokenRefreshRequestDTO tokenRefreshRequestDTO) {
        validator.validate(tokenRefreshRequestDTO).throwIfInvalid();

        TokenRefreshResponseDTO tokenRefreshResponseDTO = userService.refresh(tokenRefreshRequestDTO.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(tokenRefreshResponseDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        validator.validate(userUpdateRequestDTO).throwIfInvalid();

        User proposedUser = userMapper.fromDTO(userUpdateRequestDTO);
        User updatedUser = userService.update(id, proposedUser);
        UserDTO userDTO = userMapper.toDTO(updatedUser);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/individual")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO userDTO = userMapper.toDTO(userService.getCurrentUser());

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/individual/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        UserDTO userDTO = userMapper.toDTO(userService.getUser(id));
        userDTO = obfuscationService.obfuscate(userDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/individual/username/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable("username") String username) {
        UserDTO userDTO = userMapper.toDTO(userService.getUser(username));
        userDTO = obfuscationService.obfuscate(userDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Set<UserDTO>> getAllUsersFiltered(@RequestParam(value = "username", required = false) String username,
                                                            @RequestParam(value = "nickname", required = false) String nickname) {
        Set<UserDTO> userDTOs = userMapper.toDTO(userService.getAllUsersFiltered(username, nickname));
        userDTOs = new HashSet<>(obfuscationService.obfuscate(userDTOs.stream().toList()));

        return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
    }

}
