package com.fschoen.parlorplace.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class VoteCollection<P extends Player<?>, T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_collection_id")
    @SequenceGenerator(name = "seq_collection_id", sequenceName = "seq_collection_id")
    protected Long id;

    @ManyToOne(targetEntity = Player.class)
    @JoinColumn
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    protected P player;

    @Column(nullable = false)
    @NotNull
    protected Integer amountVotes;

    public abstract Set<T> getSubjects();

    public abstract void setSubjects(Set<T> subjects);

    public abstract Set<T> getSelection();

    public abstract void setSelection(Set<T> selection);

}
