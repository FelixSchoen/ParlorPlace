package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class WerewolfPlayerVoteCollectionDTO extends VoteCollectionDTO<WerewolfPlayerDTO> {

    @Valid
    @NotNull
    protected Set<WerewolfPlayerDTO> subjects;

    @Valid
    @NotNull
    protected Set<WerewolfPlayerDTO> selection;

}
