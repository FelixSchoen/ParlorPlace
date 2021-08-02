package com.fschoen.parlorplace.backend.controller.mapper;

import com.fschoen.parlorplace.backend.controller.dto.game.RuleSetDTO;
import com.fschoen.parlorplace.backend.entity.RuleSet;

public interface RuleSetMapper<RS extends RuleSet, RSDTO extends RuleSetDTO> {

    RSDTO toDTO(RS ruleSet);

    RS fromDTO(RSDTO ruleSetDTO);


}
