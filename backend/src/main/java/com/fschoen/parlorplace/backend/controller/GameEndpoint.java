package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameIdentifierDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.controller.mapper.GameIdentifierMapper;
import com.fschoen.parlorplace.backend.service.GameService;
import com.fschoen.parlorplace.backend.service.implementation.GameServiceImplementation;
import com.fschoen.parlorplace.backend.validation.implementation.GameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/game")
@RestController
public class GameEndpoint {

    private final GameService gameService;

    private final GameIdentifierMapper gameIdentifierMapper;

    private final GameValidator validator = new GameValidator();

    @Autowired
    public GameEndpoint(GameService gameService, GameIdentifierMapper gameIdentifierMapper) {
        this.gameService = gameService;
        this.gameIdentifierMapper = gameIdentifierMapper;
    }

    @PostMapping("/start")
    public ResponseEntity<GameIdentifierDTO> startGame(@RequestBody GameStartRequestDTO gameStartRequestDTO) {
        validator.validate(gameStartRequestDTO).throwIfInvalid();

        GameIdentifierDTO gameIdentifierDTO = gameIdentifierMapper.toDTO(gameService.start(gameStartRequestDTO.getGameType()));

        return ResponseEntity.status(HttpStatus.CREATED).body(gameIdentifierDTO);
    }

}
