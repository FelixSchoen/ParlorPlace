package com.fschoen.parlorplace.backend.utility.communication;

import com.fschoen.parlorplace.backend.controller.dto.game.LogEntryDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true, builderMethodName = "log_builder")
@Data
public class LogClientNotification<P extends PlayerDTO<?>> extends ClientNotification {

    @NotNull
    private List<LogEntryDTO<P>> logEntryList;

}
