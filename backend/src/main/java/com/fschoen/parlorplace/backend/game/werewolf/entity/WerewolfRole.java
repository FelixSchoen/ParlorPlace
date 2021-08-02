package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.*;
import lombok.*;
import lombok.experimental.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfRole extends GameRole {

    @Column(nullable = false)
    @NotNull
    private WerewolfRoleType werewolfRoleType;

}
