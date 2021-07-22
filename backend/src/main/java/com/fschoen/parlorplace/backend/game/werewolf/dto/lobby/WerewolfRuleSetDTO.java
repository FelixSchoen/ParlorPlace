package com.fschoen.parlorplace.backend.game.werewolf.dto.lobby;

import com.fschoen.parlorplace.backend.controller.dto.game.RuleSetDTO;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
