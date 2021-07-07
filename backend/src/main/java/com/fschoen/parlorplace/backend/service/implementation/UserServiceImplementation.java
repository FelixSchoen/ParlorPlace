package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enums.UserRole;
import com.fschoen.parlorplace.backend.exceptions.DataConflictException;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.UserService;
import com.fschoen.parlorplace.backend.utility.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplementation.class);

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signup(User user) throws DataConflictException {
        LOGGER.info("Signing up new User: {}", user.getUsername());

        if (userRepository.findOneByUsername(user.getUsername()).isPresent()) {
            throw new DataConflictException(Messages.getExceptionExplanationMessage("user.name.exists"));
        } else if (userRepository.findOneByEmail(user.getEmail()).isPresent()) {
            throw new DataConflictException(Messages.getExceptionExplanationMessage("user.email.exists"));
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().role(UserRole.USER).build());

        User userToPersist = user.toBuilder().nickname(user.getUsername()).password(hashedPassword).roles(roles).build();
        userToPersist.getRoles().forEach(role -> role.setUser(userToPersist));

        User createdUser = userRepository.save(userToPersist);

        return createdUser;
    }
}
