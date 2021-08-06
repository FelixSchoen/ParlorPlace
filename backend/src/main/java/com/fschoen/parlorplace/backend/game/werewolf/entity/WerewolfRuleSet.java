package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.RuleSet;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfRoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
    @ElementCollection(targetClass = WerewolfRoleType.class, fetch = FetchType.EAGER)
    List<WerewolfRoleType> gameRoles;

}
