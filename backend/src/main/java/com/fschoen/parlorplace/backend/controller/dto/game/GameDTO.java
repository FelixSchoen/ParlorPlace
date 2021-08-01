package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.enumeration.*;
import lombok.*;
import lombok.experimental.*;
import org.springframework.format.annotation.*;

import javax.validation.constraints.*;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class GameDTO {

    @NotNull
    protected Long id;

    @NotNull
    protected GameType gameType;

    @NotNull
    protected GameState gameState;

    @NotNull
    @DateTimeFormat
    protected Date startedAt;

    protected Date endedAt;

    @NotNull
    protected GameIdentifierDTO gameIdentifier;

}
