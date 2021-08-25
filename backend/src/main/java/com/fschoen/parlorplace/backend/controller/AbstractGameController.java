package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.RuleSetDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import com.fschoen.parlorplace.backend.controller.dto.lobby.LobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.mapper.GameMapper;
import com.fschoen.parlorplace.backend.controller.mapper.PlayerMapper;
import com.fschoen.parlorplace.backend.controller.mapper.RuleSetMapper;
import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.controller.mapper.VoteCollectionMapper;
import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.entity.VoteCollection;
import com.fschoen.parlorplace.backend.service.game.AbstractGameService;
import com.fschoen.parlorplace.backend.service.game.AbstractVoteService;
import com.fschoen.parlorplace.backend.service.obfuscation.ObfuscationService;
import com.fschoen.parlorplace.backend.validation.implementation.GameValidator;
import com.fschoen.parlorplace.backend.validation.implementation.VoteValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public abstract class AbstractGameController<
        G extends Game<P, RS, ?, ?>,
        P extends Player<?>,
        RS extends RuleSet,
        GDTO extends GameDTO<PDTO, RSDTO, ?, ?>,
        PDTO extends PlayerDTO<?>,
        RSDTO extends RuleSetDTO,
        LCRDTO extends LobbyChangeRequestDTO<PDTO, RSDTO>
        > {

    private final AbstractGameService<G, P, RS, ?, ?, ?> gameService;
    private final ObfuscationService<GDTO> gameObfuscationService;

    private final UserMapper userMapper;
    private final GameMapper<G, GDTO> gameMapper;
    private final PlayerMapper<P, PDTO> playerMapper;
    private final RuleSetMapper<RS, RSDTO> ruleSetMapper;

    private final GameValidator gameValidator = new GameValidator();
    private final VoteValidator voteValidator = new VoteValidator();


    public AbstractGameController(
            AbstractGameService<G, P, RS, ?, ?, ?> gameService,
            ObfuscationService<GDTO> gameObfuscationService,
            UserMapper userMapper,
            GameMapper<G, GDTO> gameMapper,
            PlayerMapper<P, PDTO> playerMapper,
            RuleSetMapper<RS, RSDTO> ruleSetMapper
    ) {
        this.gameService = gameService;
        this.gameObfuscationService = gameObfuscationService;
        this.userMapper = userMapper;
        this.gameMapper = gameMapper;
        this.playerMapper = playerMapper;
        this.ruleSetMapper = ruleSetMapper;
    }

    @PostMapping("/host")
    public ResponseEntity<GDTO> hostGame(@RequestBody GameStartRequestDTO gameStartRequestDTO) {
        gameValidator.validate(gameStartRequestDTO).throwIfInvalid();

        G game = this.gameService.initializeGame();
        game = this.gameService.joinGame(game.getGameIdentifier());

        GDTO gameDTO = gameMapper.toDTO(game);
        gameObfuscationService.obfuscate(gameDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);
    }

    @PostMapping("/join/{identifier}")
    public ResponseEntity<GDTO> joinGame(@PathVariable("identifier") String identifier) {
        G game = this.gameService.joinGame(new GameIdentifier(identifier));

        GDTO gameDTO = gameMapper.toDTO(game);
        gameObfuscationService.obfuscate(gameDTO);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @PostMapping("/quit/{identifier}")
    public ResponseEntity<Void> quitGame(@PathVariable("identifier") String identifier, @RequestBody(required = false) UserDTO userDTO) {
        if (userDTO == null)
            this.gameService.quitGame(new GameIdentifier(identifier));
        else {
            User user = this.userMapper.fromDTO(userDTO);
            this.gameService.quitGame(new GameIdentifier(identifier), user);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/lobby/change/{identifier}")
    public ResponseEntity<GDTO> changeLobby(@PathVariable("identifier") String identifier, @RequestBody LCRDTO lobbyChangeRequestDTO) {
        gameValidator.validate(lobbyChangeRequestDTO).throwIfInvalid();

        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        G game;

        this.gameService.changeGamePlayers(gameIdentifier, playerMapper.fromDTO(lobbyChangeRequestDTO.getPlayers()));
        game = this.gameService.changeGameRuleSet(gameIdentifier, ruleSetMapper.fromDTO(lobbyChangeRequestDTO.getRuleSet()));

        GDTO gameDTO = gameMapper.toDTO(game);
        gameObfuscationService.obfuscate(gameDTO);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @PostMapping("/start/{identifier}")
    public ResponseEntity<GDTO> startGame(@PathVariable("identifier") String identifier) {
        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        G game;

        game = this.gameService.startGame(gameIdentifier);

        GDTO gameDTO = gameMapper.toDTO(game);
        gameObfuscationService.obfuscate(gameDTO);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @GetMapping("/identifier/{identifier}")
    public ResponseEntity<GDTO> getActiveGame(@PathVariable("identifier") String identifier) {
        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        G game = this.gameService.getGame(gameIdentifier);

        GDTO gameDTO = gameMapper.toDTO(game);
        gameObfuscationService.obfuscate(gameDTO);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GDTO> getIndividualGame(@PathVariable("id") Long id) {
        G game = this.gameService.getGame(id);

        GDTO gameDTO = gameMapper.toDTO(game);
        gameObfuscationService.obfuscate(gameDTO);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @GetMapping("/active")
    public ResponseEntity<List<GDTO>> getUserActiveGames() {
        List<G> games = this.gameService.getUserActiveGames();

        List<GDTO> gameDTOS = gameMapper.toDTO(games);
        gameObfuscationService.obfuscate(gameDTOS);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTOS);
    }

    // TODO My thoughts behind this were as follows: If a game allows for two different types of votes (e.g. a vote on players and on other objects) I want to have as little code duplication as possible, which is why I'm providing one fixed endpoint, but the subclasses can easily reuse the vote implementation for different types of votes
    protected <C extends VoteCollection<?>, CDTO extends VoteCollectionDTO<?,?>, VS extends AbstractVoteService<?, G, ?, ?, C, ?, ?, ?, ?>, VMap extends VoteCollectionMapper<C, CDTO, ?, ?>>
    ResponseEntity<GDTO> vote(VS voteService, VMap voteCollectionMapper, String identifier, Long voteIdentifier, CDTO voteCollectionDTO) {
        voteValidator.validate(voteCollectionDTO).throwIfInvalid();

        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        C voteCollection = voteCollectionMapper.fromDTO(voteCollectionDTO);

        G game = voteService.vote(gameIdentifier, voteIdentifier, voteCollection);

        GDTO gameDTO = gameMapper.toDTO(game);
        gameObfuscationService.obfuscate(gameDTO);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

}
