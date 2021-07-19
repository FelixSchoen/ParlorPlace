package com.fschoen.parlorplace.backend.entity.persistance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class GameRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_game_role_id")
    @SequenceGenerator(name = "seq_game_role_id", sequenceName = "seq_game_role_id")
    protected Long id;

}
