package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.RuleSet;
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
    @Builder.Default
    private String resourcePack = "DEFAULT";

    @Column(nullable = false)
    @Enumerated
    @ElementCollection(targetClass = WerewolfRoleType.class, fetch = FetchType.EAGER)
    private List<WerewolfRoleType> gameRoleTypes;

}
