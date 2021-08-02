package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.*;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.*;
import lombok.*;
import lombok.experimental.*;

import javax.persistence.*;
import java.util.*;

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
