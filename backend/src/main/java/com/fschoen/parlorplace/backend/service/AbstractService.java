package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.repository.*;
import com.fschoen.parlorplace.backend.security.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import org.springframework.security.core.context.*;

import java.util.*;

public abstract class AbstractService {

    private final UserRepository userRepository;

    public AbstractService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User getPrincipal() {
        UserDetailsImplementation userDetails = (UserDetailsImplementation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> potentialUser = userRepository.findOneById(userDetails.getId());
        if (potentialUser.isEmpty())
            throw new AuthorizationException(Messages.exception("authorization.principal.empty"));
        return potentialUser.get();
    }

    protected boolean notAuthority(User user, UserRole userRole) {
        for (Role role : user.getRoles()) {
            if (role.getRole().equals(userRole)) return false;
        }
        return true;
    }

}
