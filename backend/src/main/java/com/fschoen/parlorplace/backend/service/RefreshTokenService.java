package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.persistance.RefreshToken;
import com.fschoen.parlorplace.backend.exceptions.DataConflictException;
import com.fschoen.parlorplace.backend.exceptions.TokenExpiredException;

import java.util.Optional;

public interface RefreshTokenService {

    /**
     * Finds a refresh token entity by the token itself.
     *
     * @param refreshToken Token to look for
     *
     * @return The found refresh token
     */
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    /**
     * Creates a new refresh token for the given user.
     *
     * @param userId Id of the user to create the token for
     *
     * @return A newly created refresh token that is valid for the user
     */
    RefreshToken createRefreshToken(Long userId);

    /**
     * Checks if the given refresh token is already expired, if this is the case throws a {@link TokenExpiredException}
     *
     * @param token Token to check if it is expired
     *
     * @return The unmodified token if it is not expired
     *
     * @throws TokenExpiredException If the token is expired
     */
    RefreshToken verifyExpiration(RefreshToken token) throws TokenExpiredException;

    /**
     * Deletes the token that corresponds to the user of the given id.
     *
     * @param userId Id of the user to delete the token for
     *
     * @return Integer specifying if the operation was successful
     *
     * @throws DataConflictException If no user of the given id exists
     */
    int deleteByUserId(Long userId) throws DataConflictException;

}
