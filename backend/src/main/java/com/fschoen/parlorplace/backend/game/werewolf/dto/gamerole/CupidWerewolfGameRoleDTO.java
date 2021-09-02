package com.fschoen.parlorplace.backend.game.werewolf.dto.gamerole;

import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
public class CupidWerewolfGameRoleDTO extends WerewolfGameRoleDTO {

    @Builder.Default
    @NotNull
    private WerewolfRoleType werewolfRoleType = WerewolfRoleType.CUPID;

    @Builder.Default
    @NotNull
    private WerewolfFaction werewolfFaction = WerewolfFaction.VILLAGERS;

    @Builder.Default
    private Boolean hasLinked = false;

}
