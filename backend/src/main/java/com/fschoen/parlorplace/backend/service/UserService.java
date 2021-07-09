package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.controller.dto.user.UserSigninResponseDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.exceptions.DataConflictException;

public interface UserService {

    /**
     * Creates a new user and adds it to the system.
     *
     * @param user A user object containing the information about the user to be added
     *
     * @return The created user as it is stored in the database
     *
     * @throws DataConflictException If the user cannot be added due to a data conflict
     */
    User signup(User user) throws DataConflictException;

    /**
     * Signs in an already existing user based on the given username and password combination.
     *
     * @param user The user to be signed in
     *
     * @return A response to the sign in request, containing a valid jwt token
     */
    UserSigninResponseDTO signin(User user);

}
