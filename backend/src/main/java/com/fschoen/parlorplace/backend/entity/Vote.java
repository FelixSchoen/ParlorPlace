package com.fschoen.parlorplace.backend.entity;

import com.fschoen.parlorplace.backend.enumeration.VoteState;
import com.fschoen.parlorplace.backend.enumeration.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
        D extends Enum<D>> {

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

    @OneToMany(targetEntity = VoteCollection.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id")
    )
    @MapKeyJoinColumn(name = "votecollection_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    protected Map<Long, C> voteCollectionMap;

    @Column(nullable = false)
    @NotNull
    protected Integer outcomeAmount;

    @Column(nullable = false)
    protected Instant endTime;

    public abstract Set<T> getOutcome();

    public abstract void setOutcome(Set<T> tSet);

    // TODO Not ideal, I would have preferred to have this as a field, but since enums cannot inherit I cannot specify a supertype using targetEntity=
    public abstract D getVoteDescriptor();

    public abstract void setVoteDescriptor(D d);

}
