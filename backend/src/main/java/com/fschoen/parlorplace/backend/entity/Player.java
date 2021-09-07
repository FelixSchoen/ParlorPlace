package com.fschoen.parlorplace.backend.entity;

import com.fschoen.parlorplace.backend.enumeration.CodeName;
import com.fschoen.parlorplace.backend.enumeration.LobbyRole;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Player<GR extends GameRole> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_player_id")
    @SequenceGenerator(name = "seq_player_id", sequenceName = "seq_player_id")
    protected Long id;

    @ManyToOne
    @JoinColumn
    @NotNull
    protected User user;

    @Column
    @Enumerated
    @EqualsAndHashCode.Exclude
    protected CodeName codeName;

    @ManyToOne(targetEntity = Game.class)
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    protected Game<?, ?, ?, ?> game;

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected LobbyRole lobbyRole;

    @Column(nullable = false)
    @NotNull
    protected PlayerState playerState;

    @OneToMany(mappedBy = "player", targetEntity = GameRole.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @EqualsAndHashCode.Exclude
    protected List<GR> gameRoles;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    protected Integer position;

    @Column
    @Min(1)
    protected Integer placement;

    @Column(nullable = false)
    @NotNull
    protected Boolean disconnected;

}
