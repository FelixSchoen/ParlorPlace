package com.fschoen.parlorplace.backend.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @ManyToOne(targetEntity = Player.class)
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    protected Player<?> player;

}
