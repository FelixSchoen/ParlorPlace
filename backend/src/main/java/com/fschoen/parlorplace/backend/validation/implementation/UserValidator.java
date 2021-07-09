package com.fschoen.parlorplace.backend.validation.implementation;

import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.validation.BaseValidator;
import com.fschoen.parlorplace.backend.validation.ValidationResult;

public class UserValidator extends BaseValidator {

    public ValidationResult validate(UserSignupRequestDTO userSignupRequestDTO) {
        return new ValidationResult(validateConstraints(userSignupRequestDTO));
    }

}
