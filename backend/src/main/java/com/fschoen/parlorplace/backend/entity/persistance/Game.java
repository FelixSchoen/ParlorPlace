package com.fschoen.parlorplace.backend.entity.persistance;

import com.fschoen.parlorplace.backend.game.management.GameIdentifier;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_game_instance_id")
    @SequenceGenerator(name = "seq_game_instance_id", sequenceName = "seq_game_instance_id")
    protected Long id;

    @Column(nullable = false)
    protected Date startedAt;

    @Column
    protected Date endedAt;

    @Transient
    private GameIdentifier gameIdentifier;

}
