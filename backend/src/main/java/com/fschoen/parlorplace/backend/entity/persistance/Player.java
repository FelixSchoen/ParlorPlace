package com.fschoen.parlorplace.backend.entity.persistance;

import com.fschoen.parlorplace.backend.enumeration.*;
import lombok.*;
import lombok.experimental.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
/**
 * Entity representing a player, for the DTO look for {@link com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO}
 */
public abstract class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_player_id")
    @SequenceGenerator(name = "seq_player_id", sequenceName = "seq_player_id")
    protected Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @NotNull
    protected User user;

    @Column(nullable = false)
    @NotNull
    protected Boolean disconnected;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected LobbyRole lobbyRole;

    @Column(nullable = false)
    @NotNull
    protected PlayerState playerState;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    protected Integer position;

    public abstract <G extends Game> void setGame(G game);

}
