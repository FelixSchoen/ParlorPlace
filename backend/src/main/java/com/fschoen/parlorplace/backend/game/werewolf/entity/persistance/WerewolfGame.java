package com.fschoen.parlorplace.backend.game.werewolf.entity.persistance;

import com.fschoen.parlorplace.backend.entity.persistance.*;
import com.fschoen.parlorplace.backend.enumeration.*;
import lombok.*;
import lombok.experimental.*;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfGame extends Game {

    @Column(nullable = false)
    private final GameType gameType = GameType.WEREWOLF;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<WerewolfPlayer> players;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @NotNull
    private WerewolfRuleSet ruleSet;

    @Override
    public <P extends Player> void setPlayers(Set<P> players) {
        this.players = (Set<WerewolfPlayer>) players;
    }

    @Override
    public <R extends RuleSet> void setRuleSet(R ruleSet) {
        this.ruleSet = (WerewolfRuleSet) ruleSet;
    }
}
