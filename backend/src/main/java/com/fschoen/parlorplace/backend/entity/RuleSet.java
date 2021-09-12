package com.fschoen.parlorplace.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class RuleSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_rule_set_id")
    @SequenceGenerator(name = "seq_rule_set_id", sequenceName = "seq_rule_set_id")
    protected Long id;

}
