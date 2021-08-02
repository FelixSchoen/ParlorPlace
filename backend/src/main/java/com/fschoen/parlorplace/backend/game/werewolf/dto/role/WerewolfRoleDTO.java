package com.fschoen.parlorplace.backend.game.werewolf.dto.role;

import com.fschoen.parlorplace.backend.controller.dto.game.GameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
public class WerewolfRoleDTO extends GameRoleDTO {

    @NotNull
    private WerewolfRoleType werewolfRoleType;

}
