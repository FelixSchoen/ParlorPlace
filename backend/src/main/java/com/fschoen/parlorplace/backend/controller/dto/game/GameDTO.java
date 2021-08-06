package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public abstract class GameDTO<PDTO extends PlayerDTO<?>, RSDTO extends RuleSetDTO> {

    @NotNull
    protected Long id;

    @NotNull
    protected GameType gameType;

    @NotNull
    protected GameState gameState;

    @NotNull
    protected Set<PDTO> players;

    @NotNull
    protected RSDTO ruleSet;

    @NotNull
    @Min(0)
    private Integer round;

    @NotNull
    @DateTimeFormat
    protected Date startedAt;

    protected Date endedAt;

    @NotNull
    protected GameIdentifierDTO gameIdentifier;

}
