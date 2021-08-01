package com.fschoen.parlorplace.backend.game.werewolf.dto.lobby;

import com.fschoen.parlorplace.backend.controller.dto.lobby.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.*;
import lombok.*;
import lombok.experimental.*;

import javax.validation.*;
import javax.validation.constraints.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
public class WerewolfLobbyChangeRequestDTO extends LobbyChangeRequestDTO {

    @Valid
    @NotNull
    private Set<WerewolfPlayerDTO> players;

    @Valid
    @NotNull
    private WerewolfRuleSetDTO ruleSet;

}
