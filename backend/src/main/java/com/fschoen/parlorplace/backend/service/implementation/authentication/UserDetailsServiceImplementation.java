package com.fschoen.parlorplace.backend.service.implementation.authentication;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.security.UserDetailsImplementation;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            throw new UsernameNotFoundException(Messages.exception(MessageIdentifier.USER_USERNAME_EXISTS_NOT));
        }

        return UserDetailsImplementation.fromUser(potentialUser.get());
    }

}
