package com.fschoen.parlorplace.backend.entity.persistance;

import com.fschoen.parlorplace.backend.entity.persistance.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_player_id")
    @SequenceGenerator(name = "seq_player_id", sequenceName = "seq_player_id")
    protected Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @NotNull
    protected User user;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    protected Integer position;

    public abstract <G extends Game> void setGame(G game);

}
