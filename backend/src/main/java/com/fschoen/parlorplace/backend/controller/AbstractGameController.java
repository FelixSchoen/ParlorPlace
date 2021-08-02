package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.RuleSetDTO;
import com.fschoen.parlorplace.backend.controller.dto.lobby.LobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.mapper.GameMapper;
import com.fschoen.parlorplace.backend.controller.mapper.PlayerMapper;
import com.fschoen.parlorplace.backend.controller.mapper.RuleSetMapper;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.GameRole;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.service.AbstractGameService;
import com.fschoen.parlorplace.backend.validation.implementation.GameValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class AbstractGameController<
        G extends Game<P, RS>,
        P extends Player<GR>,
        RS extends RuleSet,
        GR extends GameRole,
        GDTO extends GameDTO<?, ?>,
        PDTO extends PlayerDTO<?>,
        RSDTO extends RuleSetDTO,
        LCRDTO extends LobbyChangeRequestDTO<PDTO, RSDTO>
        > {

    private final AbstractGameService<G, P, RS, GR, ? extends GameRepository<G>> gameService;

    private final GameMapper<G, GDTO> gameMapper;
    private final PlayerMapper<P, PDTO> playerMapper;
    private final RuleSetMapper<RS, RSDTO> ruleSetMapper;

    private final GameValidator validator = new GameValidator();

    public AbstractGameController(AbstractGameService<G, P, RS, GR, ? extends GameRepository<G>> gameService, UserMapper userMapper, GameMapper<G, GDTO> gameMapper, PlayerMapper<P, PDTO> playerMapper, RuleSetMapper<RS, RSDTO> ruleSetMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
        this.playerMapper = playerMapper;
        this.ruleSetMapper = ruleSetMapper;
    }

    @PostMapping("/host")
    public ResponseEntity<GDTO> hostGame(@RequestBody GameStartRequestDTO gameStartRequestDTO) {
        validator.validate(gameStartRequestDTO).throwIfInvalid();

        G game = this.gameService.initializeGame();
        game = this.gameService.joinGame(game.getGameIdentifier());

        GDTO gameDTO = gameMapper.toDTO(game);

        return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);
    }

    @PostMapping("/join/{identifier}")
    public ResponseEntity<GameDTO> joinGame(@PathVariable("identifier") String identifier) {
        G game = this.gameService.joinGame(new GameIdentifier(identifier));

        GDTO gameDTO = gameMapper.toDTO(game);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @PostMapping("/quit/{identifier}")
    public ResponseEntity<Void> quitGame(@PathVariable("identifier") String identifier, @RequestBody(required = false) UserDTO userDTO) {
        if (userDTO == null)
            this.gameService.quitGame(new GameIdentifier(identifier));
        else
            throw new NotImplementedException("Not implemented");

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/lobby/change/{identifier}")
    public ResponseEntity<GDTO> changeLobby(@PathVariable("identifier") String identifier, @RequestBody LCRDTO lobbyChangeRequestDTO) {
        validator.validate(lobbyChangeRequestDTO).throwIfInvalid();

        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        G game;

        this.gameService.changeGamePlayers(gameIdentifier, playerMapper.fromDTO(lobbyChangeRequestDTO.getPlayers()));
        game = this.gameService.changeGameRuleSet(gameIdentifier, ruleSetMapper.fromDTO(lobbyChangeRequestDTO.getRuleSet()));

        GDTO gameDTO = gameMapper.toDTO(game);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @GetMapping("/state/game/{identifier}")
    public ResponseEntity<GDTO> getGameState(@PathVariable("identifier") String identifier) {
        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        G game = this.gameService.getGame(gameIdentifier);
        GDTO gameDTO = gameMapper.toDTO(game);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

}
