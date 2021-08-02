package com.fschoen.parlorplace.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;

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
