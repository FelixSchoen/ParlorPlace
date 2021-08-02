package com.fschoen.parlorplace.backend.entity;

import com.fschoen.parlorplace.backend.enumeration.*;
import com.fschoen.parlorplace.backend.game.management.*;
import lombok.*;
import lombok.experimental.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_game_instance_id")
    @SequenceGenerator(name = "seq_game_instance_id", sequenceName = "seq_game_instance_id")
    protected Long id;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected GameState gameState;

    @Column(nullable = false)
    protected Date startedAt;

    @Column
    protected Date endedAt;

    @Transient
    private GameIdentifier gameIdentifier;

    public abstract GameType getGameType();

    public abstract <P extends Player> Set<P> getPlayers();

    public abstract <P extends Player> void setPlayers(Set<P> players);

    public abstract <R extends RuleSet> R getRuleSet();

    public abstract <R extends RuleSet> void setRuleSet(R ruleSet);

}
