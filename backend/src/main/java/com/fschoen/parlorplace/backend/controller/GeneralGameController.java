package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.dto.game.GameBaseInformationDTO;
import com.fschoen.parlorplace.backend.controller.mapper.GameIdentifierMapper;
import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.entity.GameIdentifier;
import com.fschoen.parlorplace.backend.service.AbstractGameService;
import com.fschoen.parlorplace.backend.service.GeneralGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/game/general")
@RestController
public class GeneralGameController {

    private final GeneralGameService gameService;
    private final AbstractGameService<?, ?, ?, ?, ?> abstractService;

    private final GameIdentifierMapper gameIdentifierMapper;

    @Autowired
    public GeneralGameController(GeneralGameService gameService, AbstractGameService<?, ?, ?, ?, ?> abstractService, GameIdentifierMapper gameIdentifierMapper) {
        this.gameService = gameService;
        this.abstractService = abstractService;
        this.gameIdentifierMapper = gameIdentifierMapper;
    }

    @GetMapping("/base_info/{identifier}")
    public ResponseEntity<GameBaseInformationDTO> getGame(@PathVariable("identifier") String identifier) {
        GameIdentifier gameIdentifier = new GameIdentifier(identifier);
        Game<?, ?> game = this.abstractService.getGame(gameIdentifier);
        GameBaseInformationDTO gameBaseInformationDTO = GameBaseInformationDTO.builder()
                .gameIdentifier(this.gameIdentifierMapper.toDTO(game.getGameIdentifier()))
                .gameType(game.getGameType())
                .gameState(game.getGameState()).build();

        return ResponseEntity.status(HttpStatus.OK).body(gameBaseInformationDTO);
    }



}
