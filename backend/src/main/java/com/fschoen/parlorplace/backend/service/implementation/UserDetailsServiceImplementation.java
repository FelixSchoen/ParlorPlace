package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.persistent.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.utility.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

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
            throw new UsernameNotFoundException(Messages.getExceptionExplanationMessage("user.name.exists.not"));
        }

        User user = potentialUser.get();
        // TODO Return roles
        return withUsername(user.getUsername())
                .password(user.getPassword())
                .build();
    }

}
