package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.controller.dto.authentication.*;
import com.fschoen.parlorplace.backend.controller.dto.user.*;
import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.repository.*;
import com.fschoen.parlorplace.backend.security.*;
import com.fschoen.parlorplace.backend.service.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Slf4j
@Service
public class UserServiceImplementation extends AbstractService implements UserService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImplementation(RefreshTokenService refreshTokenService, UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        super(userRepository);
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public User register(User user) throws DataConflictException {
        log.info("Signing up User: {}", user.getUsername());

        if (userRepository.findOneByUsername(user.getUsername()).isPresent())
            throw new DataConflictException(Messages.exception(MessageIdentifiers.USER_USERNAME_EXISTS));
        else if (userRepository.findOneByEmail(user.getEmail()).isPresent())
            throw new DataConflictException(Messages.exception(MessageIdentifiers.USER_EMAIL_EXISTS));

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        Set<Role> roles = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};

        User persistUser = user.toBuilder().nickname(user.getNickname()).password(hashedPassword).roles(roles).build();
        persistUser.getRoles().forEach(role -> role.setUser(persistUser));

        return userRepository.save(persistUser);
    }

    public UserLoginResponseDTO login(User user) {
        log.info("Signing in User: {}", user.getUsername());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        String accessToken = jwtUtils.generateJwtToken(userDetails);

        Set<UserRole> roles = userDetails.getAuthorities().stream().map(item -> UserRole.valueOf(item.getAuthority())).collect(Collectors.toSet());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return UserLoginResponseDTO.builder().id(userDetails.getId()).username(userDetails.getUsername()).roles(roles).accessToken(accessToken).refreshToken(refreshToken.getRefreshToken()).build();
    }

    @Override
    public TokenRefreshResponseDTO refresh(String refreshToken) throws AuthorizationException {
        log.info("Refreshing Token");

        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtils.generateTokenFromUsername(user.getUsername());
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
                    return TokenRefreshResponseDTO.builder().accessToken(accessToken).refreshToken(newRefreshToken.getRefreshToken()).build();
                })
                .orElseThrow(() -> new AuthorizationException(Messages.exception(MessageIdentifiers.AUTHORIZATION_TOKEN_REFRESH_EXISTS_NOT)));
    }

    @Override
    public User update(Long id, User user) throws AuthorizationException, DataConflictException {
        log.info("Updating User: {}", user.getUsername());

        User principal = getPrincipal();

        if ((!principal.getId().equals(id) && notAuthority(principal, UserRole.ROLE_ADMIN))
                || (user.getRoles() != null && !user.getRoles().equals(principal.getRoles()) && notAuthority(principal, UserRole.ROLE_ADMIN))
                || (user.getUsername() != null && !user.getUsername().equals("") && !user.getUsername().equals(principal.getUsername()) && notAuthority(principal, UserRole.ROLE_ADMIN)))
            throw new AuthorizationException(Messages.exception(MessageIdentifiers.AUTHORIZATION_UNAUTHORIZED));

        if (user.getId() != null && !user.getId().equals(id))
            throw new DataConflictException(Messages.exception(MessageIdentifiers.DATA_MISMATCHED_ID));

        User existingUser = userRepository.findOneById(id).orElseThrow(() -> new DataConflictException(Messages.exception(MessageIdentifiers.USER_ID_EXISTS_NOT)));
        User.UserBuilder persistUserBuilder = existingUser.toBuilder();

//        if (user.getUsername() != null)
//            persistUserBuilder.username(user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            persistUserBuilder.password(hashedPassword);
        }
        if (user.getNickname() != null && !user.getNickname().isEmpty())
            persistUserBuilder.nickname(user.getNickname());
        if (user.getEmail() != null && !user.getEmail().isEmpty())
            persistUserBuilder.email(user.getEmail());
        if (user.getRoles() != null)
            persistUserBuilder.roles(user.getRoles());

        User persistUser = persistUserBuilder.build();
        persistUser.getRoles().forEach(role -> role.setUser(persistUser));

        return userRepository.save(persistUser);
    }

    @Override
    public User getCurrentUser() throws DataConflictException {
        log.info("Obtaining current user");

        User principal = getPrincipal();

        if (principal == null)
            throw new DataConflictException(Messages.exception(MessageIdentifiers.USER_EXISTS_NOT));

        return principal;
    }

    @Override
    public User getUser(Long id) throws DataConflictException {
        log.info("Obtaining user with id: {}", id);

        User existingUser = userRepository.findOneById(id).orElseThrow(() -> new DataConflictException(Messages.exception(MessageIdentifiers.USER_ID_EXISTS_NOT)));

        return existingUser;
    }

    @Override
    public User getUser(String username) throws DataConflictException {
        log.info("Obtaining user with username: {}", username);

        User existingUser = userRepository.findOneByUsername(username).orElseThrow(() -> new DataConflictException(Messages.exception(MessageIdentifiers.USER_USERNAME_EXISTS_NOT)));

        return existingUser;
    }

    @Override
    public Set<User> getAllUsersFiltered(String username, String nickname) {
        log.info("Obtaining all users with username: {}, nickname: {}", username, nickname);

        Set<User> userSet = new HashSet<>();

        if (username != null && username.length() >= 3)
            userSet.addAll(userRepository.findAllByUsernameContainsIgnoreCase(username));

        if (nickname != null && nickname.length() >= 3)
            userSet.addAll(userRepository.findAllByNicknameContainsIgnoreCase(nickname));

        return userSet;
    }

}
