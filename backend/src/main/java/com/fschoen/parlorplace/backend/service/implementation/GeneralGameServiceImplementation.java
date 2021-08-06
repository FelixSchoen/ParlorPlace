package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.repository.GeneralGameRepository;
import com.fschoen.parlorplace.backend.service.GeneralGameService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifiers;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class GeneralGameServiceImplementation implements GeneralGameService {

    private final GeneralGameRepository gameRepository;

    @Autowired
    public GeneralGameServiceImplementation(GeneralGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostConstruct
    private void onInit() {
        // Remove orphaned Games
        List<Game<?, ?, ?>> orphanedGames = gameRepository.findAllByGameState(GameState.LOBBY);
        gameRepository.deleteAll(orphanedGames);
    }

    public Game<?, ?, ?> getGameBaseInformation(GameIdentifier gameIdentifier) {
        List<Game<?, ?, ?>> games = this.gameRepository.findAllByGameIdentifier_TokenAndEndedAt(gameIdentifier.getToken(), null);

        if (games.size() == 0)
            throw new GameException(Messages.exception(MessageIdentifiers.GAME_EXISTS_NOT));
        if (games.size() > 1)
            throw new DataConflictException(Messages.exception(MessageIdentifiers.GAME_UNIQUE_NOT));

        return games.get(0);
    }

}
