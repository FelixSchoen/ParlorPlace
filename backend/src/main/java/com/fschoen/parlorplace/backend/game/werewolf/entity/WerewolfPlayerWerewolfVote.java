package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.Vote;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteIdentifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@Entity
public class WerewolfPlayerWerewolfVote extends WerewolfVote<WerewolfPlayer, WerewolfPlayerVoteCollection> {

    @Column(nullable = false)
    @Enumerated
    @Builder.Default
    @NotNull
    private WerewolfVoteIdentifier voteIdentifier = WerewolfVoteIdentifier.PLAYER_VOTE;

    @ManyToMany(targetEntity = Player.class)
    @JoinTable(
            joinColumns = @JoinColumn(name = "vote_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<WerewolfPlayer> outcome;

}
