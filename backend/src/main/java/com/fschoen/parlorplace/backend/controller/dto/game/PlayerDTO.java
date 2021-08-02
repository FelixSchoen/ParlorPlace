package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfGameRoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
    protected Boolean disconnected;

    @NotNull
    protected LobbyRole lobbyRole;

    @NotNull
    protected PlayerState playerState;

    @NotNull
    @Min(0)
    protected Integer position;

}
