package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfVoteIdentifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class WerewolfPlayerWerewolfVoteDTO extends WerewolfVoteDTO<WerewolfPlayerDTO, WerewolfPlayerVoteCollectionDTO> {

    @Builder.Default
    @NotNull
    protected WerewolfVoteIdentifier voteIdentifier = WerewolfVoteIdentifier.PLAYER_VOTE;

}
