package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.*;
import com.fschoen.parlorplace.backend.security.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service("UserDetailsServiceImplementation")
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> potentialUser = userRepository.findOneByUsername(s);

        if (potentialUser.isEmpty()) {
            throw new UsernameNotFoundException(Messages.exception(MessageIdentifiers.USER_USERNAME_EXISTS_NOT));
        }

        return UserDetailsImplementation.fromUser(potentialUser.get());
    }

}
