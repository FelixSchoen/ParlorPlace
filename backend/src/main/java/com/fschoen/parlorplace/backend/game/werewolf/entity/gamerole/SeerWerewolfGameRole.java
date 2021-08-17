package com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole;

import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class SeerWerewolfGameRole extends WerewolfGameRole {

    @Column(nullable = false)
    @Builder.Default
    @NotNull
    protected WerewolfRoleType werewolfRoleType = WerewolfRoleType.SEER;

    @Column(nullable = false)
    @Builder.Default
    @NotNull
    protected WerewolfFaction werewolfFaction = WerewolfFaction.VILLAGERS;

}
