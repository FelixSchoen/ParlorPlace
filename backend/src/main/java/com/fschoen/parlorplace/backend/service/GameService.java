package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;

import java.util.Set;

public interface GameService {

    /**
     * Starts a new game of the given type.
     *
     * @param gameType The type of the game to start
     * @return The generated {@link com.fschoen.parlorplace.backend.game.management.GameInstance} of the newly created game
     * @throws GameException If the user trying to create the game already is in a game
     */
    GameIdentifier start(GameType gameType) throws GameException;

    /**
     * Issues a lobby change request, which can change e.g. the seating position of the players present.
     *
     * @param gameIdentifier The game identifier of the lobby to change
     * @param players        A set of players representing the changes to be made
     * @throws GameException If no game could be found using the identifier, the player issuing the request is not in the lobby, or the lobby cannot be edited
     */
    void changeLobby(GameIdentifier gameIdentifier, Set<Player> players) throws GameException;

    GameIdentifier generateValidGameIdentifier();

}
