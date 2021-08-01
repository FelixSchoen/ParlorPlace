package com.fschoen.parlorplace.backend.game.werewolf.dto.lobby;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.*;
import lombok.*;
import lombok.experimental.*;

import javax.validation.constraints.*;
import java.util.*;

@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
public class WerewolfRuleSetDTO extends RuleSetDTO {

    @NotNull
    List<WerewolfRoleType> roles;

}
