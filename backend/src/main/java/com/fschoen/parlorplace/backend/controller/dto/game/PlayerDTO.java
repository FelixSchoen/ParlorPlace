package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.enumeration.CodeName;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class PlayerDTO<GRDTO extends GameRoleDTO> {

    @NotNull
    protected Long id;

    @Valid
    @NotNull
    protected UserDTO user;

    protected CodeName codeName;

    @NotNull
    protected LobbyRole lobbyRole;

    @NotNull
    protected PlayerState playerState;

    @Valid
    @NotNull
    protected List<GRDTO> gameRoles;

    @NotNull
    @Min(0)
    protected Integer position;

    @Min(1)
    protected Integer placement;

    @NotNull
    protected Boolean disconnected;

}
