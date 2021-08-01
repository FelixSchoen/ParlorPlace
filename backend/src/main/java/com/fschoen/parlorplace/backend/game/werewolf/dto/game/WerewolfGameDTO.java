package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.*;
import lombok.*;
import lombok.experimental.*;

import javax.validation.constraints.*;
import java.util.*;

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
