package com.fschoen.parlorplace.backend.controller.dto.lobby;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfLobbyChangeRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder(toBuilder = true)
// TODO Better way of serializing?
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WerewolfLobbyChangeRequestDTO.class, name = "WerewolfLobbyChangeRequestDTO")}
)
@Data
public abstract class LobbyChangeRequestDTO {

    public abstract Set<? extends PlayerDTO> getPlayers();

}
