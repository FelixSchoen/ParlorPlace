package com.fschoen.parlorplace.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
// TODO I really dislike this class - all it does is to provide the Player.class annotation for hibernate, otherwise I could have made VoteCollection generic... Is there a way around this?
public abstract class PlayerVoteCollection<P extends Player<?>, T extends Player<?>> extends VoteCollection<P> {

    @ManyToMany(targetEntity = Player.class)
    @JoinTable(
            joinColumns = @JoinColumn(name = "playervotecollection_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    protected Set<T> subjects;

    @ManyToMany(targetEntity = Player.class)
    @JoinTable(
            joinColumns = @JoinColumn(name = "playervotecollection_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    protected Set<T> selection;

}