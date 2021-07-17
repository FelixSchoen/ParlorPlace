package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.persistance.RefreshToken;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.TokenExpiredException;
import com.fschoen.parlorplace.backend.repository.RefreshTokenRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.RefreshTokenService;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImplementation implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${fschoen.parlorplace.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    public RefreshTokenServiceImplementation(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findOneByRefreshToken(refreshToken);
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        User user = userRepository.findById(userId).orElseThrow(() -> new DataConflictException(Messages.getExceptionExplanationMessage("user.id.exists.not")));

        refreshTokenRepository.removeByUser(user);

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) throws TokenExpiredException {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException(token.getRefreshToken(), Messages.getExceptionExplanationMessage("authorization.token.refresh.expired"));
        }

        return token;
    }

    @Override
    @Transactional
    public int deleteByUserId(Long userId) throws DataConflictException {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataConflictException(Messages.getExceptionExplanationMessage("user.id.exists.not")));
        return refreshTokenRepository.removeByUser(user);
    }

}
