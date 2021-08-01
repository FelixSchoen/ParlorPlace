package com.fschoen.parlorplace.backend.game.werewolf.dto.role;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.*;
import lombok.*;
import lombok.experimental.*;

import javax.validation.constraints.*;

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
