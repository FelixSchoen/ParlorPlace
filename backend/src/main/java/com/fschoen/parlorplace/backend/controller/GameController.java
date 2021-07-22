package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.lobby.LobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.controller.mapper.*;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.validation.implementation.GameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/game")
@RestController
public class GameController {

    private final GameService gameService;

    private final UserMapper userMapper;
    private final GameMapper gameMapper;
    private final PlayerMapper playerMapper;
    private final RuleSetMapper ruleSetMapper;
    private final GameIdentifierMapper gameIdentifierMapper;

    private final GameValidator validator = new GameValidator();

    @Autowired
    public GameController(GameService gameService, UserMapper userMapper, GameMapper gameMapper, PlayerMapper playerMapper, RuleSetMapper ruleSetMapper, GameIdentifierMapper gameIdentifierMapper) {
        this.gameService = gameService;
        this.userMapper = userMapper;
        this.gameMapper = gameMapper;
        this.playerMapper = playerMapper;
        this.ruleSetMapper = ruleSetMapper;
        this.gameIdentifierMapper = gameIdentifierMapper;
    }

    @PostMapping("/start")
    public ResponseEntity<GameDTO> startGame(@RequestBody GameStartRequestDTO gameStartRequestDTO) {
        validator.validate(gameStartRequestDTO).throwIfInvalid();

        GameDTO gameDTO = gameMapper.toDTO(gameService.create(gameStartRequestDTO.getGameType()), true);

        return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);
    }

    @PostMapping("/join/{identifier}")
    public ResponseEntity<GameDTO> joinGame(@PathVariable("identifier") String identifier) {
        GameDTO gameDTO = gameMapper.toDTO(gameService.join(new GameIdentifier(identifier)), true);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @PostMapping("/quit/{identifier}")
    public ResponseEntity<Void> quitGame(@PathVariable("identifier") String identifier, @RequestBody(required = false) UserDTO user) {
        gameService.quit(new GameIdentifier(identifier), userMapper.fromDTO(user));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/lobby/change/{identifier}")
    public ResponseEntity<GameDTO> changeLobby(@PathVariable("identifier") String identifier, @RequestBody LobbyChangeRequestDTO lobbyChangeRequestDTO) {
        validator.validate(lobbyChangeRequestDTO).throwIfInvalid();

        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        gameService.changeLobby(gameIdentifier, playerMapper.fromDTO(lobbyChangeRequestDTO.getPlayers()));
        GameDTO game = gameMapper.toDTO(gameService.changeLobby(gameIdentifier, ruleSetMapper.fromDTO(lobbyChangeRequestDTO.getRuleSet())), true);

        return ResponseEntity.status(HttpStatus.OK).body(game);
    }

    @GetMapping("/state/game/{identifier}")
    public ResponseEntity<GameDTO> getGameState(@PathVariable("identifier") String identifier) {
        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        GameDTO game = gameMapper.toDTO(gameService.getGameState(gameIdentifier), true);

        return ResponseEntity.status(HttpStatus.OK).body(game);
    }

}