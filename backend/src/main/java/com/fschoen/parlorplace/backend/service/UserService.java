package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.controller.dto.authentication.TokenRefreshResponseDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSigninResponseDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.exceptions.AuthorizationException;
import com.fschoen.parlorplace.backend.exceptions.DataConflictException;

import java.util.Set;

public interface UserService {

    /**
     * Creates a new user and adds it to the system.
     *
     * @param user A user object containing the information about the user to be added
     * @return The created user as it is stored in the database
     * @throws DataConflictException If the user cannot be added due to a data conflict
     */
    User signup(User user) throws DataConflictException;

    /**
     * Signs in an already existing user based on the given username and password combination.
     *
     * @param user The user to be signed in
     * @return A response to the sign in request, containing a valid jwt token
     */
    UserSigninResponseDTO signin(User user);

    TokenRefreshResponseDTO refresh(String refreshToken);

    /**
     * Updates the user given by its id using the supplied argument.
     *
     * @param id   Id of the user to be updated
     * @param user The user object containing the id of the user to be updated and the update information
     * @return The updated user
     * @throws AuthorizationException If the principal does not have the necessary authority to edit the specified user
     * @throws DataConflictException  If the given Id and the one of the {@param user} do not match or no user with the given id was found
     */
    User update(Long id, User user) throws AuthorizationException, DataConflictException;

    /**
     * Obtains the currently logged-in user based on the authentication.
     *
     * @return The currently logged-in user
     * @throws DataConflictException If no current user could be found
     */
    User getCurrentUser() throws DataConflictException;

    /**
     * Obtains the user with the given id if it exists.
     *
     * @param id Id of the user to obtain
     * @return The found user if it exists
     * @throws DataConflictException If no such user exists
     */
    User getUser(Long id) throws DataConflictException;

    /**
     * Obtains all users where either their usernames contain the string given by {@param username} or their nicknames
     * contain the string given by {@param nickname}, where both of these parameters must be of length greater than 3 to
     * be queried.
     *
     * @param username The string to compare usernames against
     * @param nickname The string to compare nicknames against
     * @return All found users
     */
    Set<User> getAllUsersFiltered(String username, String nickname);

}
