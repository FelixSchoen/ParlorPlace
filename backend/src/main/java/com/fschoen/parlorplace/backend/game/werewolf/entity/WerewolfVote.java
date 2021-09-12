package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.Vote;
import com.fschoen.parlorplace.backend.entity.VoteCollection;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteIdentifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class WerewolfVote<T, C extends VoteCollection<T>> extends Vote<WerewolfPlayer, T, C, WerewolfVoteIdentifier, WerewolfVoteDescriptor> {

    @Column(nullable = false)
    @Enumerated
    @NotNull
    protected WerewolfVoteDescriptor voteDescriptor;

    public abstract WerewolfVoteIdentifier getVoteIdentifier();

    public abstract void setVoteIdentifier(WerewolfVoteIdentifier voteIdentifier);
}
