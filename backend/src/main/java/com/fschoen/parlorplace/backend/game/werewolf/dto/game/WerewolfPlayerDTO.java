package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.*;
import lombok.*;
import lombok.experimental.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
public class WerewolfPlayerDTO extends PlayerDTO {

    private WerewolfRoleDTO werewolfRoleDTO;

}
