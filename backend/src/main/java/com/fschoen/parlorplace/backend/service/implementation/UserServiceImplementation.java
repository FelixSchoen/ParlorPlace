package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.controller.dto.authentication.TokenRefreshResponseDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSigninResponseDTO;
import com.fschoen.parlorplace.backend.entity.persistance.RefreshToken;
import com.fschoen.parlorplace.backend.entity.persistance.Role;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.entity.transience.UserDetailsImplementation;
import com.fschoen.parlorplace.backend.enums.UserRole;
import com.fschoen.parlorplace.backend.exceptions.AuthorizationException;
import com.fschoen.parlorplace.backend.exceptions.DataConflictException;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.security.JwtUtils;
import com.fschoen.parlorplace.backend.service.AbstractService;
import com.fschoen.parlorplace.backend.service.RefreshTokenService;
import com.fschoen.parlorplace.backend.service.UserService;
import com.fschoen.parlorplace.backend.utility.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation extends AbstractService implements UserService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplementation.class);

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
    public User signup(User user) throws DataConflictException {
        LOGGER.info("Signing up User: {}", user.getUsername());

        if (userRepository.findOneByUsername(user.getUsername()).isPresent()) {
            throw new DataConflictException(Messages.getExceptionExplanationMessage("user.name.exists"));
        } else if (userRepository.findOneByEmail(user.getEmail()).isPresent()) {
            throw new DataConflictException(Messages.getExceptionExplanationMessage("user.email.exists"));
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        Set<Role> roles = new HashSet<>() {{
            add(Role.builder().role(UserRole.ROLE_USER).build());
        }};

        User persistUser = user.toBuilder().nickname(user.getNickname()).password(hashedPassword).roles(roles).build();
        persistUser.getRoles().forEach(role -> role.setUser(persistUser));

        return userRepository.save(persistUser);
    }

    public UserSigninResponseDTO signin(User user) {
        LOGGER.info("Signing in User: {}", user.getUsername());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        String accessToken = jwtUtils.generateJwtToken(userDetails);

        Set<UserRole> roles = userDetails.getAuthorities().stream().map(item -> UserRole.valueOf(item.getAuthority())).collect(Collectors.toSet());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return UserSigninResponseDTO.builder().id(userDetails.getId()).username(userDetails.getUsername()).roles(roles).accessToken(accessToken).refreshToken(refreshToken.getRefreshToken()).build();
    }

    @Override
    public TokenRefreshResponseDTO refresh(String refreshToken) throws AuthorizationException {
        LOGGER.info("Refreshing Token");

        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtils.generateTokenFromUsername(user.getUsername());
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
                    return TokenRefreshResponseDTO.builder().accessToken(accessToken).refreshToken(newRefreshToken.getRefreshToken()).build();
                })
                .orElseThrow(() -> new AuthorizationException(Messages.getExceptionExplanationMessage("authorization.token.refresh.exists.not")));
    }

    @Override
    public User update(Long id, User user) throws AuthorizationException, DataConflictException {
        LOGGER.info("Updating User: {}", user.getUsername());

        User principal = getPrincipal();

        if ((!principal.getId().equals(id) && !hasAuthority(principal, UserRole.ROLE_ADMIN))
                || (user.getRoles() != null && !user.getRoles().equals(principal.getRoles()) && !hasAuthority(principal, UserRole.ROLE_ADMIN))
                || (user.getUsername() != null && !user.getUsername().equals("") && !user.getUsername().equals(principal.getUsername()) && !hasAuthority(principal, UserRole.ROLE_ADMIN)))
            throw new AuthorizationException(Messages.getExceptionExplanationMessage("authorization.unauthorized"));

        if (user.getId() != null && !user.getId().equals(id))
            throw new DataConflictException(Messages.getExceptionExplanationMessage("data.mismatched.id"));

        User existingUser = userRepository.findOneById(id).orElseThrow(() -> new DataConflictException(Messages.getExceptionExplanationMessage("user.id.exists.not")));
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
        LOGGER.info("Obtaining current user");

        User principal = getPrincipal();

        if (principal == null)
            throw new DataConflictException(Messages.getExceptionExplanationMessage("user.exists.not"));

        return principal;
    }

    @Override
    public User getUser(Long id) throws DataConflictException {
        LOGGER.info("Obtaining user with id: {}", id);

        User existingUser = userRepository.findOneById(id).orElseThrow(() -> new DataConflictException(Messages.getExceptionExplanationMessage("user.id.exists.not")));

        return existingUser;
    }

    @Override
    public User getUser(String username) throws DataConflictException {
        LOGGER.info("Obtaining user with username: {}", username);

        User existingUser = userRepository.findOneByUsername(username).orElseThrow(() -> new DataConflictException(Messages.getExceptionExplanationMessage("user.username.exists.not")));

        return existingUser;
    }

    @Override
    public Set<User> getAllUsersFiltered(String username, String nickname) {
        LOGGER.info("Obtaining all users with username: {}, nickname: {}", username, nickname);

        Set<User> userSet = new HashSet<>();

        if (username != null && username.length() >= 3)
            userSet.addAll(userRepository.findAllByUsernameContainsIgnoreCase(username));

        if (nickname != null && nickname.length() >= 3)
            userSet.addAll(userRepository.findAllByNicknameContainsIgnoreCase(nickname));

        return userSet;
    }

}
