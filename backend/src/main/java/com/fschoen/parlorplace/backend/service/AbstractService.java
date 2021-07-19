package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.entity.transience.UserDetailsImplementation;
import com.fschoen.parlorplace.backend.enumeration.UserRole;
import com.fschoen.parlorplace.backend.exception.AuthorizationException;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public abstract class AbstractService {

    private final UserRepository userRepository;

    public AbstractService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User getPrincipal() {
        UserDetailsImplementation userDetails = (UserDetailsImplementation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> potentialUser = userRepository.findOneById(userDetails.getId());
        if (potentialUser.isEmpty()) throw new AuthorizationException(Messages.exception("authorization.principal.empty"));
        return potentialUser.get();
    }

    protected boolean notAuthority(User user, UserRole userRole) {
        for (Role role: user.getRoles()) {
            if (role.getRole().equals(userRole)) return false;
        }
        return true;
    }

}
