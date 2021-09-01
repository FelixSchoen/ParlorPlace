package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteDTO;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteDescriptor;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteIdentifier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "voteIdentifier")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WerewolfPlayerWerewolfVoteDTO.class, name = "PLAYER")}
)
@Data
public abstract class WerewolfVoteDTO<T, C extends VoteCollectionDTO<T>> extends VoteDTO<WerewolfPlayerDTO, T, C, WerewolfVoteIdentifier, WerewolfVoteDescriptor> {

    @NotNull
    protected WerewolfVoteIdentifier voteIdentifier;

    @NotNull
    protected WerewolfVoteDescriptor voteDescriptor;

}
