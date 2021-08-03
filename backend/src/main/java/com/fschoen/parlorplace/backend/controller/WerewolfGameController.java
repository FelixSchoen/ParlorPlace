package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.controller.mapper.UserMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfLobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfRuleSetDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGame;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfGameMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfPlayerMapper;
import com.fschoen.parlorplace.backend.game.werewolf.mapper.WerewolfRuleSetMapper;
import com.fschoen.parlorplace.backend.repository.GameRepository;
import com.fschoen.parlorplace.backend.service.AbstractGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/game/werewolf")
@RestController
public class WerewolfGameController extends AbstractGameController<
        WerewolfGame,
        WerewolfPlayer,
        WerewolfRuleSet,
        WerewolfGameRole,
        WerewolfGameDTO,
        WerewolfPlayerDTO,
        WerewolfRuleSetDTO,
        WerewolfLobbyChangeRequestDTO
        > {

    @Autowired
    public WerewolfGameController(AbstractGameService<WerewolfGame, WerewolfPlayer, WerewolfRuleSet, WerewolfGameRole, ? extends GameRepository<WerewolfGame>> gameService, UserMapper userMapper, WerewolfGameMapper gameMapper, WerewolfPlayerMapper playerMapper, WerewolfRuleSetMapper ruleSetMapper) {
        super(gameService, userMapper, gameMapper, playerMapper, ruleSetMapper);
    }

}
