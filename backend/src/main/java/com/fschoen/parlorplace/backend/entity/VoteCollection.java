package com.fschoen.parlorplace.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class VoteCollection<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_collection_id")
    @SequenceGenerator(name = "seq_collection_id", sequenceName = "seq_collection_id")
    protected Long id;

    @Column(nullable = false)
    @NotNull
    protected Integer amountVotes;

    @Column(nullable = false)
    protected Boolean allowAbstain;

    @Column
    protected Boolean abstain;

    public abstract Set<T> getSubjects();

    public abstract void setSubjects(Set<T> subjects);

    public abstract Set<T> getSelection();

    public abstract void setSelection(Set<T> selection);

}
