package com.fschoen.parlorplace.backend.service;

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

}
