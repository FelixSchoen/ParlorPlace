package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.VoteCollection;
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
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfVoteCollection extends VoteCollection<WerewolfPlayer> {

    @ManyToMany(targetEntity = Player.class)
    @JoinTable(
            joinColumns = @JoinColumn(name = "votecollection_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<WerewolfPlayer> subjects;

    @ManyToMany(targetEntity = Player.class)
    @JoinTable(
            joinColumns = @JoinColumn(name = "votecollection_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<WerewolfPlayer> selection;

}
