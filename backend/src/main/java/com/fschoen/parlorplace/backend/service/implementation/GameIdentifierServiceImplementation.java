package com.fschoen.parlorplace.backend.service.implementation;

import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.service.GameIdentifierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class GameIdentifierServiceImplementation implements GameIdentifierService {

    private final GameRepository<?> gameRepository;

    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Autowired
    public GameIdentifierServiceImplementation(GameRepository<?> gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameIdentifier generateValidGameIdentifier() {
        int standardLength = 4;

        while (true) {
            GameIdentifier gameIdentifier = generateToken(standardLength);
            if (this.gameRepository.findAllByGameIdentifier_TokenAndEndedAt(gameIdentifier.getToken(), null).stream().anyMatch(game -> game.getGameIdentifier().equals(gameIdentifier)))
                standardLength++;
            else
                return gameIdentifier;
        }
    }

    private GameIdentifier generateToken(int length) {
        Random random = new Random();
        StringBuilder tokenBuilder = new StringBuilder();

        while (tokenBuilder.length() < length) {
            int index = random.nextInt(CHARSET.length());
            tokenBuilder.append(CHARSET.charAt(index));
        }

        return new GameIdentifier(tokenBuilder.toString());
    }

}
