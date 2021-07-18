package com.fschoen.parlorplace.backend.game.werewolf.entity.persistance;

import com.fschoen.parlorplace.backend.entity.persistance.GameRole;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@Entity
public class WerewolfRole extends GameRole {

    @Column(nullable = false)
    @NotNull
    private RoleType roleType;

}
