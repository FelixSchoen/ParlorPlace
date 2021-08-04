package com.fschoen.parlorplace.backend.repository;

import com.fschoen.parlorplace.backend.entity.RefreshToken;
import com.fschoen.parlorplace.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds a refresh token by its id.
     *
     * @param id Id of the refresh token
     * @return The found refresh token
     */
    Optional<RefreshToken> findOneById(Long id);

    /**
     * Finds a refresh token by the token itself.
     *
     * @param refreshToken Token to look for
     * @return The found refresh token
     */
    Optional<RefreshToken> findOneByRefreshToken(String refreshToken);

    /**
     * Deletes all refresh tokens that belong to the given user.
     *
     * @param user The user to delete the refresh token for
     */
    int removeByUser(User user);

}
