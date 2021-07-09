package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.entity.transience.UserDetailsImplementation;
import com.fschoen.parlorplace.backend.enums.UserRole;
import com.fschoen.parlorplace.backend.exceptions.AuthorizationException;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.utility.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public abstract class AbstractService {

    private final UserRepository userRepository;

    @Autowired
    public AbstractService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User getPrincipal() {
        UserDetailsImplementation userDetails = (UserDetailsImplementation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> potentialUser = userRepository.findOneById(userDetails.getId());
        if (potentialUser.isEmpty()) throw new AuthorizationException(Messages.getExceptionExplanationMessage("authorization.principal.empty"));
        return potentialUser.get();
    }

    protected boolean hasAuthority(User user, UserRole userRole) {
        for (Role role: user.getRoles()) {
            if (role.getRole().equals(userRole)) return true;
        }
        return false;
    }

}
