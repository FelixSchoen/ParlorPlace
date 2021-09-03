package com.fschoen.parlorplace.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fschoen.parlorplace.backend.controller.dto.user.UserRegisterRequestDTO;
import com.fschoen.parlorplace.backend.entity.Role;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class ParlorPlaceApplication implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ParlorPlaceApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParlorPlaceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        List<String> defaultUsersFiles = args.getOptionValues("default.users");
        if (defaultUsersFiles != null) {
            for (String defaultUsersFile : defaultUsersFiles) {
                File file = new File("./" + defaultUsersFile);
                ObjectMapper mapper = new ObjectMapper();

                UserRegisterRequestDTO[] userRegisterRequestDTOS = mapper.readValue(file, UserRegisterRequestDTO[].class);

                for (UserRegisterRequestDTO userRegisterRequestDTO : userRegisterRequestDTOS) {
                    User user = new User();

                    String hashedPassword = passwordEncoder.encode(userRegisterRequestDTO.getPassword());
                    Set<Role> roles = new HashSet<>() {{
                        add(Role.builder().role(UserRole.ROLE_USER).build());
                        add(Role.builder().role(UserRole.ROLE_ADMIN).build());
                    }};

                    User persistUser = user.toBuilder()
                            .username(userRegisterRequestDTO.getUsername())
                            .nickname(userRegisterRequestDTO.getNickname())
                            .password(hashedPassword)
                            .email(userRegisterRequestDTO.getEmail())
                            .roles(roles).build();
                    persistUser.getRoles().forEach(role -> role.setUser(persistUser));

                    user = userRepository.save(persistUser);
                    log.info("Saved default user {}", user.getUsername());
                }
            }
        }
    }
}