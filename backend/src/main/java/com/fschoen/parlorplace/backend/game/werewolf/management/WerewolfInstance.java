package com.fschoen.parlorplace.backend.game.werewolf.management;

import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.entity.transience.GameState;
import com.fschoen.parlorplace.backend.game.management.GameInstance;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.repository.PlayerRepository;
import com.fschoen.parlorplace.backend.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope("prototype")
@Slf4j
public class WerewolfInstance extends GameInstance {

    @Autowired
    public WerewolfInstance(GameService gameService, GameRepository<WerewolfGame> gameRepository, PlayerRepository<WerewolfPlayer> playerRepository, WerewolfManager werewolfManager) {
        super(gameService, gameRepository, playerRepository, werewolfManager);
    }

    @PostConstruct
    public void init() {
        WerewolfGame werewolfGame = WerewolfGame.builder().players(new HashSet<>()).startedAt(new Date()).gameIdentifier(this.gameIdentifier).build();
        werewolfGame = getGameRepository().save(werewolfGame);
        this.gameId = werewolfGame.getId();

        log.info("Created new Werewolf instance: {}", this.gameIdentifier);
    }

    @Override
    public Game join(User user) {
        WerewolfGame game = (WerewolfGame) getGame();
        WerewolfPlayer werewolfPlayer = WerewolfPlayer.builder().user(user).werewolfGame(game).position(getPlayers().size()).build();

        game.getPlayers().add(werewolfPlayer);
        game = getGameRepository().save(game);

        return game;
    }

    @Override
    public Set<WerewolfPlayer> getPlayers() {
        return ((WerewolfGame) this.getGame()).getPlayers();
    }

    @Override
    protected WerewolfGameRepository getGameRepository() {
        return (WerewolfGameRepository) this.gameRepository;
    }

}
