package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.user.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import lombok.*;
import lombok.experimental.*;

import javax.validation.*;
import javax.validation.constraints.*;

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
