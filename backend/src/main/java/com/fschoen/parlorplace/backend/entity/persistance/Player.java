package com.fschoen.parlorplace.backend.entity.persistance;

import com.fschoen.parlorplace.backend.entity.persistance.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Entity
public abstract class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_player_id")
    @SequenceGenerator(name = "seq_player_id", sequenceName = "seq_player_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    private User user;

    @NotNull
    @Min(0)
    private Integer position;

}
