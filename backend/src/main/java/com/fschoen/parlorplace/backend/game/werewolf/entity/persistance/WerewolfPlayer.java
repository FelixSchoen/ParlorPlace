package com.fschoen.parlorplace.backend.game.werewolf.entity.persistance;

import com.fschoen.parlorplace.backend.entity.persistance.Player;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@Entity
public class WerewolfPlayer extends Player {

    @ManyToOne
    @JoinColumn(name = "werewolfgame_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private WerewolfGame werewolfGame;

    @OneToOne
    @JoinColumn(name = "werewolfrole_id", referencedColumnName = "id")
    private WerewolfRole werewolfRole;

}
