package com.fschoen.parlorplace.backend.entity.persistance;

import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
public abstract class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_game_instance_id")
    @SequenceGenerator(name = "seq_game_instance_id", sequenceName = "seq_game_instance_id")
    private Long id;

    @Column(nullable = false)
    private GameType gameType;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    @EqualsAndHashCode.Exclude
    @NotNull
    private Set<Player> players;

    @Transient
    private GameIdentifier gameIdentifier;

}
