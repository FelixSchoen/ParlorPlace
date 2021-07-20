package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameIdentifierDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.lobby.LobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.controller.mapper.GameIdentifierMapper;
import com.fschoen.parlorplace.backend.controller.mapper.GameMapper;
import com.fschoen.parlorplace.backend.controller.mapper.PlayerMapper;
import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.validation.implementation.GameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/game")
@RestController
public class GameController {

    private final GameService gameService;

    private final GameMapper gameMapper;
    private final PlayerMapper playerMapper;
    private final GameIdentifierMapper gameIdentifierMapper;

    private final GameValidator validator = new GameValidator();

    @Autowired
    public GameController(GameService gameService, GameMapper gameMapper, PlayerMapper playerMapper, GameIdentifierMapper gameIdentifierMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
        this.playerMapper = playerMapper;
        this.gameIdentifierMapper = gameIdentifierMapper;
    }

    @PostMapping("/start")
    public ResponseEntity<GameDTO> startGame(@RequestBody GameStartRequestDTO gameStartRequestDTO) {
        validator.validate(gameStartRequestDTO).throwIfInvalid();

        GameDTO gameDTO = gameMapper.toDTO(gameService.create(gameStartRequestDTO.getGameType()), true);

        return ResponseEntity.status(HttpStatus.CREATED).body(gameDTO);
    }

    @PostMapping("/join/{identifier}")
    public ResponseEntity<GameDTO> startGame(@PathVariable("identifier") String identifier) {
        GameDTO gameDTO = gameMapper.toDTO(gameService.join(new GameIdentifier(identifier)), true);

        return ResponseEntity.status(HttpStatus.OK).body(gameDTO);
    }

    @PostMapping("/lobby/change/{identifier}")
    public ResponseEntity<GameDTO> changeLobby(@PathVariable("identifier") String identifier, @RequestBody LobbyChangeRequestDTO lobbyChangeRequestDTO) {
        validator.validate(lobbyChangeRequestDTO).throwIfInvalid();

        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        GameDTO game = gameMapper.toDTO(gameService.changeLobby(gameIdentifier, playerMapper.fromDTO(lobbyChangeRequestDTO.getPlayers())), true);

        return ResponseEntity.status(HttpStatus.OK).body(game);
    }

}
