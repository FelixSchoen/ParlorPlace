package com.fschoen.parlorplace.backend.entity;

import com.fschoen.parlorplace.backend.enumeration.VoteDrawStrategy;
import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Vote<
        P extends Player<?>,
        T,
        C extends VoteCollection<T>,
        D extends Enum<D>,
        E extends Enum<E>> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_vote_id")
    @SequenceGenerator(name = "seq_vote_id", sequenceName = "seq_vote_id")
    protected Long id;

    @ManyToOne(targetEntity = Game.class)
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    protected Game<?, ?, ?, ?> game;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected VoteState voteState;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected VoteType voteType;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected VoteDrawStrategy voteDrawStrategy;

    @ManyToMany(targetEntity = Player.class, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    protected Set<P> voters;

    @OneToMany(targetEntity = VoteCollection.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id")
    )
    @MapKeyJoinColumn(name = "votecollection_id")
    @NotNull
    protected Map<Long, C> voteCollectionMap;

    @Column(nullable = false)
    @NotNull
    protected Integer outcomeAmount;

    @Column(nullable = false)
    @NotNull
    protected Integer round;

    @Column(nullable = false)
    protected Instant endTime;

    public abstract Set<T> getOutcome();

    public abstract void setOutcome(Set<T> tSet);

    // TODO Not ideal, I would have preferred to have this as a field, but since enums cannot inherit I cannot specify a supertype using targetEntity=

    public abstract D getVoteIdentifier();

    public abstract void setVoteIdentifier(D d);

    public abstract E getVoteDescriptor();

    public abstract void setVoteDescriptor(E e);

}
