package com.fschoen.parlorplace.backend.game.werewolf.dto.game;

import com.fschoen.parlorplace.backend.controller.dto.game.LogEntryDTO;
import com.fschoen.parlorplace.backend.game.werewolf.enumeration.WerewolfLogType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@Data
public class WerewolfLogEntryDTO extends LogEntryDTO<WerewolfPlayerDTO> {

    @NotNull
    private WerewolfLogType logType;

}
