package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.repository.GeneralGameRepository;
import com.fschoen.parlorplace.backend.service.GeneralGameService;
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
        List<Game<?,?>> orphanedGames = gameRepository.findAllByGameState(GameState.LOBBY);
        gameRepository.deleteAll(orphanedGames);
    }
}
