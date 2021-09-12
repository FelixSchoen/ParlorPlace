package com.fschoen.parlorplace.backend.game.werewolf.entity.gamerole;

import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfGameRole;
import com.fschoen.parlorplace.backend.game.werewolf.entity.WerewolfPlayer;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfFaction;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class BodyguardWerewolfGameRole extends WerewolfGameRole {

    @Column(nullable = false)
    @Builder.Default
    @NotNull
    private WerewolfRoleType werewolfRoleType = WerewolfRoleType.BODYGUARD;

    @Column(nullable = false)
    @Builder.Default
    @NotNull
    private WerewolfFaction werewolfFaction = WerewolfFaction.VILLAGERS;

    @ManyToOne(targetEntity = WerewolfPlayer.class)
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    protected WerewolfPlayer lastProtected;

}
