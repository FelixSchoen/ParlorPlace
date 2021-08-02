package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.repository.*;
import com.fschoen.parlorplace.backend.service.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.time.*;
import java.util.*;

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

        User user = userRepository.findById(userId).orElseThrow(() -> new DataConflictException(Messages.exception("user.id.exists.not")));

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
            throw new TokenExpiredException(token.getRefreshToken(), Messages.exception("authorization.token.refresh.expired"));
        }

        return token;
    }

    @Override
    @Transactional
    public int deleteByUserId(Long userId) throws DataConflictException {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataConflictException(Messages.exception("user.id.exists.not")));
        return refreshTokenRepository.removeByUser(user);
    }

}
