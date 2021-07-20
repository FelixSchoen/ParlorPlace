package com.fschoen.parlorplace.backend.game.werewolf.entity.persistance;

import com.fschoen.parlorplace.backend.entity.persistance.RuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
@Entity
public class WerewolfRuleSet extends RuleSet {

    @Column(nullable = false)
    @Enumerated
    @ElementCollection(targetClass = WerewolfRoleType.class)
    List<WerewolfRoleType> roles;

}
