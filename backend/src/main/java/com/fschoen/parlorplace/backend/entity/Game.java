package com.fschoen.parlorplace.backend.entity;

import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Game<P extends Player<?>, RS extends RuleSet, V extends Vote<P, ?, ?, ?, ?>, L extends LogEntry<?>> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_game_instance_id")
    @SequenceGenerator(name = "seq_game_instance_id", sequenceName = "seq_game_instance_id")
    protected Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(referencedColumnName = "id")
    @NotNull
    protected GameIdentifier gameIdentifier;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected GameState gameState;

    @OneToMany(mappedBy = "game", targetEntity = Player.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @EqualsAndHashCode.Exclude
    @NotNull
    protected Set<P> players;

    @OneToOne(targetEntity = RuleSet.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @NotNull
    protected RS ruleSet;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    protected Integer round;

    @OneToMany(mappedBy = "game", targetEntity = Vote.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    protected List<V> votes;

    @OneToMany(mappedBy = "game", targetEntity = LogEntry.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    protected List<L> log;

    @Column(nullable = false)
    protected Instant startedAt;

    @Column
    protected Instant endedAt;

    public abstract GameType getGameType();

}
