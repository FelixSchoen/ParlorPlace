package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;

import java.util.Set;

public interface GameService {

    /**
     * Created a new game of the given type.
     *
     * @param gameType The type of the game to start
     * @return The newly created game
     * @throws GameException If the user trying to create the game already is in a game
     */
    Game create(GameType gameType) throws GameException;

    /**
     * Issues a join request to the game instance given by {@param gameIdentifier}.
     *
     * @param gameIdentifier The identifier of the game
     * @return The modified game
     * @throws GameException         If the game has already started or the player is already contained in the game
     * @throws DataConflictException If there was a type mismatch regarding the type of game
     */
    Game join(GameIdentifier gameIdentifier) throws GameException, DataConflictException;

    /**
     * Issues a lobby change request, which can change e.g. the seating position of the players present.
     *
     * @param gameIdentifier The game identifier of the lobby to change
     * @param players        A set of players representing the changes to be made
     * @return The modified game
     * @throws GameException         If no game could be found using the identifier, the player issuing the request is not in the lobby, or the lobby cannot be edited
     * @throws DataConflictException If the request is faulty
     */
    Game changeLobby(GameIdentifier gameIdentifier, Set<Player> players) throws GameException, DataConflictException;

    GameIdentifier generateValidGameIdentifier();

}
