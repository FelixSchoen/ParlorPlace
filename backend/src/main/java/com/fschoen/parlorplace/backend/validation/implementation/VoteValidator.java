package com.fschoen.parlorplace.backend.validation.implementation;

import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import com.fschoen.parlorplace.backend.validation.BaseValidator;
import com.fschoen.parlorplace.backend.validation.ValidationResult;

public class VoteValidator extends BaseValidator {

    public ValidationResult validate(VoteCollectionDTO<?> voteCollectionDTO) {
        return new ValidationResult(validateConstraints(voteCollectionDTO, true));
    }

}
