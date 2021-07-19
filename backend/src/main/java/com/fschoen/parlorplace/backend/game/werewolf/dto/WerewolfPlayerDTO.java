package com.fschoen.parlorplace.backend.game.werewolf.dto;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfRoleDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
public class WerewolfPlayerDTO extends PlayerDTO {

    @NotNull
    private Long id;

    @NotNull
    private UserDTO user;

    @NotNull
    @Min(0)
    private Integer position;

    private WerewolfRoleDTO werewolfRoleDTO;

}
