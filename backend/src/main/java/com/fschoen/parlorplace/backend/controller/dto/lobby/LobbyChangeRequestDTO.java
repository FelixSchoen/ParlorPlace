package com.fschoen.parlorplace.backend.controller.dto.lobby;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class LobbyChangeRequestDTO {

    protected Set<PlayerDTO> playerDTOSet;

}
