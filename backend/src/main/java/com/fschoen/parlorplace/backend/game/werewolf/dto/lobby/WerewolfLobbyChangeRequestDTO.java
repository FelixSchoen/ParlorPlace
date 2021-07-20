package com.fschoen.parlorplace.backend.game.werewolf.dto.lobby;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.lobby.LobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
