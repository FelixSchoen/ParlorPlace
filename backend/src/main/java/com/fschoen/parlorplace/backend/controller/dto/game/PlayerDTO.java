package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.utility.obfuscation.Obfuscateable;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class PlayerDTO {

    @NotNull
    private Long id;

    @NotNull
    private UserDTO user;

    @NotNull
    private PlayerState playerState;

    @NotNull
    @Min(0)
    private Integer position;

}
