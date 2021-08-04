package com.fschoen.parlorplace.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

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
