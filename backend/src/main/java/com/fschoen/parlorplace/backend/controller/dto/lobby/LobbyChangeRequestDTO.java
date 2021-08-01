package com.fschoen.parlorplace.backend.controller.dto.lobby;

import com.fasterxml.jackson.annotation.*;
import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;

@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder(toBuilder = true)
// TODO Better way of serializing?
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "$class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WerewolfLobbyChangeRequestDTO.class, name = "WerewolfLobbyChangeRequestDTO")}
)
@Data
public abstract class LobbyChangeRequestDTO {

    public abstract Set<? extends PlayerDTO> getPlayers();

    public abstract RuleSetDTO getRuleSet();

}
