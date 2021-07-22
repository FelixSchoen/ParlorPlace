package com.fschoen.parlorplace.backend.controller.dto.lobby;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.RuleSetDTO;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfLobbyChangeRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

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
