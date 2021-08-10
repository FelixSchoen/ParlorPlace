package com.fschoen.parlorplace.backend.game.werewolf.entity;

import com.fschoen.parlorplace.backend.entity.Vote;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@Entity
public class WerewolfVote extends Vote<WerewolfPlayer, WerewolfVoteCollection, WerewolfVoteDescriptor> {

    @Column(nullable = false)
    @Enumerated
    @NotNull
    private WerewolfVoteDescriptor voteDescriptor;

}
