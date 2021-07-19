package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;

public interface GameService {

    /**
     * Starts a new game of the given type.
     *
     * @param gameType The type of the game to start
     * @return The generated {@link com.fschoen.parlorplace.backend.game.management.GameInstance} of the newly created game
     * @throws GameException If the user trying to create the game already is in a game
     */
    GameIdentifier start(GameType gameType) throws GameException;

    GameIdentifier generateValidGameIdentifier();

}
