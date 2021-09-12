package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.Player;
import com.fschoen.parlorplace.backend.entity.VoteCollection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;


@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfPlayerVoteCollection extends VoteCollection<WerewolfPlayer> {

    @ManyToMany(targetEntity = Player.class, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "votecollection_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @NotNull
    private Set<WerewolfPlayer> subjects;

    @ManyToMany(targetEntity = Player.class, fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "votecollection_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @NotNull
    private Set<WerewolfPlayer> selection;

}
