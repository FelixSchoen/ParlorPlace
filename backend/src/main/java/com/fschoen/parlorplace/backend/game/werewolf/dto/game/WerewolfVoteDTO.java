package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.game.VoteDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class WerewolfVoteDTO extends VoteDTO<WerewolfPlayerDTO, WerewolfPlayerVoteCollectionDTO> {
}
