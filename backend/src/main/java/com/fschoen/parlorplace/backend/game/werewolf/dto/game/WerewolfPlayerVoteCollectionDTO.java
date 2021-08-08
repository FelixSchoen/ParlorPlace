package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerVoteCollectionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class WerewolfPlayerVoteCollectionDTO extends PlayerVoteCollectionDTO<WerewolfPlayerDTO, WerewolfPlayerDTO> {
}
