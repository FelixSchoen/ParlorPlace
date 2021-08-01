package com.fschoen.parlorplace.backend.validation.implementation;

import com.fschoen.parlorplace.backend.controller.dto.game.*;
import com.fschoen.parlorplace.backend.controller.dto.lobby.*;
import com.fschoen.parlorplace.backend.validation.*;

public class GameValidator extends BaseValidator {

    public ValidationResult validate(GameStartRequestDTO gameStartRequestDTO) {
        return new ValidationResult(validateConstraints(gameStartRequestDTO, false));
    }

    public ValidationResult validate(LobbyChangeRequestDTO lobbyChangeRequestDTO) {
        return new ValidationResult(validateConstraints(lobbyChangeRequestDTO, true));
    }

}
