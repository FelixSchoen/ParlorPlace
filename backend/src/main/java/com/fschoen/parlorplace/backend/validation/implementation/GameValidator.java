package com.fschoen.parlorplace.backend.validation.implementation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameStartRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.validation.BaseValidator;
import com.fschoen.parlorplace.backend.validation.ValidationResult;

public class GameValidator extends BaseValidator {

    public ValidationResult validate(GameStartRequestDTO gameStartRequestDTO) {
        return new ValidationResult(validateConstraints(gameStartRequestDTO, false));
    }

}
