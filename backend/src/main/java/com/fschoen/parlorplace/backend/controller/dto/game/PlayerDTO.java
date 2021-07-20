package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.lobby.WerewolfLobbyChangeRequestDTO;
import com.fschoen.parlorplace.backend.utility.obfuscation.Obfuscateable;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class PlayerDTO {

    @NotNull
    protected Long id;

    @NotNull
    @Valid
    protected UserDTO user;

    @NotNull
    protected LobbyRole lobbyRole;

    @NotNull
    protected PlayerState playerState;

    @NotNull
    @Min(0)
    protected Integer position;

}
