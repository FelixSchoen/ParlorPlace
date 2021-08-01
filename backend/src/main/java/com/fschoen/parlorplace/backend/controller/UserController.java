package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.authentication.*;
import com.fschoen.parlorplace.backend.controller.dto.user.*;
import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.entity.persistance.*;
import com.fschoen.parlorplace.backend.service.*;
import com.fschoen.parlorplace.backend.validation.implementation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserValidator validator = new UserValidator();

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        validator.validate(userRegisterRequestDTO).throwIfInvalid();

        User proposedUser = userMapper.fromDTO(userRegisterRequestDTO);
        User createdUser = userService.register(proposedUser);
        UserDTO userDTO = userMapper.toDTO(createdUser, false);

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
        UserDTO userDTO = userMapper.toDTO(updatedUser, false);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/individual")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO userDTO = userMapper.toDTO(userService.getCurrentUser(), false);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/individual/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        UserDTO userDTO = userMapper.toDTO(userService.getUser(id), true);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("/individual/username/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable("username") String username) {
        UserDTO userDTO = userMapper.toDTO(userService.getUser(username), true);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Set<UserDTO>> getAllUsersFiltered(@RequestParam(value = "username", required = false) String username,
                                                            @RequestParam(value = "nickname", required = false) String nickname) {
        Set<UserDTO> userDTOs = userMapper.toDTO(userService.getAllUsersFiltered(username, nickname), true);

        return ResponseEntity.status(HttpStatus.OK).body(userDTOs);
    }

}
