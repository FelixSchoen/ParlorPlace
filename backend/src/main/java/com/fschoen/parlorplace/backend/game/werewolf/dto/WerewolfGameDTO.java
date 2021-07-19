package com.fschoen.parlorplace.backend.game.werewolf.dto;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import lombok.*;
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
    private Set<PlayerDTO> players;

}
