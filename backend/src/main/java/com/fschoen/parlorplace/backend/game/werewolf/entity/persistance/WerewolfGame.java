package com.fschoen.parlorplace.backend.game.werewolf.entity.persistance;

import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfGame extends Game {

    @Column(nullable = false)
    private final GameType gameType = GameType.WEREWOLF;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<WerewolfPlayer> players;


    @Override
    public <P extends Player> void setPlayers(Set<P> players) {
        this.players = (Set<WerewolfPlayer>) players;
    }
}
