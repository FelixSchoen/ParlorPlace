package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.RuleSetDTO;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.entity.persistance.RuleSet;
import com.fschoen.parlorplace.backend.exception.MappingException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfRuleSetDTO;
import com.fschoen.parlorplace.backend.game.werewolf.entity.persistance.WerewolfRuleSet;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RuleSetMapper {

    WerewolfRuleSetDTO toDTO(WerewolfRuleSet werewolfRuleSet, @Context Boolean obfuscate);

    WerewolfRuleSet fromDTO(WerewolfRuleSetDTO werewolfRuleSetDTO);

    default RuleSetDTO toDTO(RuleSet ruleSet, @Context Boolean obfuscate) {
        if (ruleSet instanceof WerewolfRuleSet) {
            return toDTO((WerewolfRuleSet) ruleSet, obfuscate);
        }

        throw new MappingException(Messages.exception("mapping.type"));
    }

    default RuleSet fromDTO(RuleSetDTO ruleset) {
        if (ruleset instanceof WerewolfRuleSetDTO) {
            return fromDTO((WerewolfRuleSetDTO) ruleset);
        }

        throw new MappingException(Messages.exception("mapping.type"));
    }

}
