package com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole;

import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WitchWerewolfGameRole extends WerewolfGameRole {

    @Column(nullable = false)
    @Builder.Default
    @NotNull
    private WerewolfRoleType werewolfRoleType = WerewolfRoleType.WITCH;

    @Column(nullable = false)
    @Builder.Default
    @NotNull
    private WerewolfFaction werewolfFaction = WerewolfFaction.VILLAGERS;

    @Column(nullable = false)
    @Builder.Default
    @Accessors(fluent = true)
    @NotNull
    private boolean hasHealed = false;

    @Column(nullable = false)
    @Builder.Default
    @Accessors(fluent = true)
    @NotNull
    private boolean hasKilled = false;

}
