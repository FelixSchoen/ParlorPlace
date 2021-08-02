package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfRuleSetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
public class WerewolfGameDTO extends GameDTO {

    @NotNull
    private GameType gameType = GameType.WEREWOLF;

    @NotNull
    private Set<WerewolfPlayerDTO> players;

    @NotNull
    private WerewolfRuleSetDTO ruleSet;

}
