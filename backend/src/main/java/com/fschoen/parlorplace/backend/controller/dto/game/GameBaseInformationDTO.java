package com.fschoen.parlorplace.backend.controller.dto.game;

import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public class GameBaseInformationDTO {

    @NotNull
    private GameIdentifierDTO gameIdentifier;

    @NotNull
    private GameType gameType;

    @NotNull
    private GameState gameState;

}
