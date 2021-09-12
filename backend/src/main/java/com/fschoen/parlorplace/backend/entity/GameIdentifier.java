package com.fschoen.parlorplace.backend.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Data
@Entity
public class GameIdentifier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_game_identifier_id")
    @SequenceGenerator(name = "seq_game_identifier_id", sequenceName = "seq_game_identifier_id")
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotNull
    @Size(min = 4)
    private String token;

    public GameIdentifier(String token) {
        this.token = token.toUpperCase();
    }


}
