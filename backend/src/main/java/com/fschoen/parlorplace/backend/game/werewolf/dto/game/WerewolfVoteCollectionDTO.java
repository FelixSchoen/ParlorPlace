package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class WerewolfVoteCollectionDTO extends VoteCollectionDTO<WerewolfPlayerDTO, WerewolfPlayerDTO> {

    @NotNull
    protected Set<WerewolfPlayerDTO> subjects;

    @NotNull
    protected Set<WerewolfPlayerDTO> selection;

}
