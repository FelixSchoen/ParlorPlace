package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.Game;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfGamePhase;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfGame extends Game<WerewolfPlayer, WerewolfRuleSet, WerewolfVote<?, ?>, WerewolfLogEntry> {

    @Column(nullable = false)
    private final GameType gameType = GameType.WEREWOLF;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    private WerewolfGamePhase gamePhase;

}
