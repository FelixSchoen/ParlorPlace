package com.fschoen.parlorplace.backend.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class LogEntry<P extends Player<?>> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_log_entry_id")
    @SequenceGenerator(name = "seq_log_entry_id", sequenceName = "seq_log_entry_id")
    protected Long id;

    @Column(nullable = false)
    @NotNull
    protected UUID identifier;

    @ManyToOne(targetEntity = Game.class)
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    protected Game<?, ?, ?, ?> game;

    @ManyToMany(targetEntity = Player.class, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "logentry_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    protected Set<P> recipients;

    @ManyToMany(targetEntity = Player.class, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "logentry_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @NotNull
    protected Set<P> sources = new HashSet<>();

    @ManyToMany(targetEntity = Player.class, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "logentry_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @NotNull
    protected Set<P> targets = new HashSet<>();

}
