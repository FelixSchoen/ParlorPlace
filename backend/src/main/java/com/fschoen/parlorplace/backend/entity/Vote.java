package com.fschoen.parlorplace.backend.entity;

import com.fschoen.parlorplace.backend.enumeration.VoteState;
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
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Vote<
        P extends Player<?>,
        C extends VoteCollection> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_vote_id")
    @SequenceGenerator(name = "seq_vote_id", sequenceName = "seq_vote_id")
    protected Long id;

    @ManyToOne(targetEntity = Game.class)
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    protected Game<?, ?, ?> game;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected VoteState voteState;

    @OneToMany(targetEntity = VoteCollection.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            joinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "id")
    )
    @MapKeyJoinColumn(name = "player_id")
    //@LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    protected Map<P, C> voteCollectionMap;


}
