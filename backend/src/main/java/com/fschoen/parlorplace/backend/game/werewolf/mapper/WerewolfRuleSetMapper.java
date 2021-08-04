package com.fschoen.parlorplace.backend.game.werewolf.mapper;

import com.fschoen.parlorplace.backend.controller.mapper.RuleSetMapper;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfRuleSetDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfRuleSet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WerewolfRuleSetMapper extends RuleSetMapper<WerewolfRuleSet, WerewolfRuleSetDTO> {
}
