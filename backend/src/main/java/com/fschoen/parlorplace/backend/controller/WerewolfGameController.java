package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerVoteCollectionDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfLobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfRuleSetDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfGameMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfPlayerMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfPlayerVoteCollectionMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfRuleSetMapper;
import com.fschoen.parlorplace.backend.game.werewolf.repository.WerewolfGameRepository;
import com.fschoen.parlorplace.backend.game.werewolf.service.WerewolfGameModerator;
import com.fschoen.parlorplace.backend.game.werewolf.service.WerewolfPlayerWerewolfVoteService;
import com.fschoen.parlorplace.backend.service.game.AbstractGameService;
import com.fschoen.parlorplace.backend.service.obfuscation.ObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/game/werewolf")
@RestController
public class WerewolfGameController extends AbstractGameController<
        WerewolfGame,
        WerewolfPlayer,
        WerewolfRuleSet,
        WerewolfGameDTO,
        WerewolfPlayerDTO,
        WerewolfRuleSetDTO,
        WerewolfLobbyChangeRequestDTO> {

    private final WerewolfPlayerWerewolfVoteService voteService;

    private final WerewolfPlayerVoteCollectionMapper voteCollectionMapper;

    @Autowired
    public WerewolfGameController(
            AbstractGameService<WerewolfGame, WerewolfPlayer, WerewolfRuleSet, WerewolfGameRole, WerewolfGameRepository, WerewolfGameModerator> gameService,
            ObfuscationService<WerewolfGameDTO> gameObfuscationService,
            WerewolfPlayerWerewolfVoteService voteService,
            UserMapper userMapper,
            WerewolfGameMapper gameMapper,
            WerewolfPlayerMapper playerMapper,
            WerewolfRuleSetMapper ruleSetMapper,
            WerewolfPlayerVoteCollectionMapper voteCollectionMapper
    ) {
        super(gameService, gameObfuscationService, userMapper, gameMapper, playerMapper, ruleSetMapper);
        this.voteService = voteService;
        this.voteCollectionMapper = voteCollectionMapper;
    }

    @PostMapping("/vote/{identifier}/{voteIdentifier}")
    public ResponseEntity<WerewolfGameDTO> vote(@PathVariable("identifier") String identifier, @PathVariable("voteIdentifier") Long voteIdentifier, @RequestBody WerewolfPlayerVoteCollectionDTO voteCollectionDTO) {
        return this.vote(voteService, voteCollectionMapper, identifier, voteIdentifier, voteCollectionDTO);
    }

}