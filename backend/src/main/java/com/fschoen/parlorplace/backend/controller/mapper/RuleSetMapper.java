package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.exception.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.*;
import com.fschoen.parlorplace.backend.game.werewolf.entity.*;
import com.fschoen.parlorplace.backend.utility.messaging.*;
import org.mapstruct.*;

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
