package com.fschoen.parlorplace.backend.game.werewolf.entity.persistance;

import com.fschoen.parlorplace.backend.entity.persistance.Game;
import com.fschoen.parlorplace.backend.entity.persistance.Player;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfPlayer extends Player {

    @ManyToOne
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private WerewolfGame game;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private WerewolfRole werewolfRole;

    @Override
    public <G extends Game> void setGame(G game) {
        this.game = (WerewolfGame) game;
    }

}
