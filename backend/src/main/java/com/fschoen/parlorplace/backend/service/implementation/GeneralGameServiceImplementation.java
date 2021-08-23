package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.repository.GeneralGameRepository;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.BaseService;
import com.fschoen.parlorplace.backend.service.game.GeneralGameService;
import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GeneralGameServiceImplementation extends BaseService implements GeneralGameService{

    private final GeneralGameRepository gameRepository;

    @Autowired
    public GeneralGameServiceImplementation(UserRepository userRepository, GeneralGameRepository gameRepository) {
        super(userRepository);
        this.gameRepository = gameRepository;
    }

    @PostConstruct
    private void onInit() {
        // Remove orphaned Games

        // Remove games stuck in lobby
        List<Game<?, ?, ?, ?>> orphanedLobbyGames = gameRepository.findAllByGameState(GameState.LOBBY);
        gameRepository.deleteAll(orphanedLobbyGames);

        // Remove games ongoing longer than 12 hours
        List<Game<?, ?, ?, ?>> orphanedOngoingGames = gameRepository.findAllByEndedAt(null);
        orphanedOngoingGames = orphanedLobbyGames.stream()
                .filter(game -> TimeUnit.HOURS.convert(new Date().getTime() - game.getStartedAt().getTime(), TimeUnit.MILLISECONDS) > 12)
                .collect(Collectors.toList());
        gameRepository.deleteAll(orphanedOngoingGames);
    }

    public Game<?, ?, ?, ?> getActiveGameBaseInformation(GameIdentifier gameIdentifier) {
        List<Game<?, ?, ?, ?>> games = this.gameRepository.findAllByGameIdentifier_TokenAndEndedAt(gameIdentifier.getToken(), null);

        if (games.size() == 0)
            throw new GameException(Messages.exception(MessageIdentifier.GAME_EXISTS_NOT));
        if (games.size() > 1)
            throw new DataConflictException(Messages.exception(MessageIdentifier.GAME_UNIQUE_NOT));

        return games.get(0);
    }

    public Game<?, ?, ?, ?> getIndividualGameBaseInformation(Long id) {
        log.info("User {} obtaining game {}", getPrincipal(), id);

        Optional<Game<?, ?, ?, ?>> game = this.gameRepository.findById(id);

        if (game.isEmpty())
            throw new GameException(Messages.exception(MessageIdentifier.GAME_EXISTS_NOT));

        return game.orElseThrow();
    }

}
