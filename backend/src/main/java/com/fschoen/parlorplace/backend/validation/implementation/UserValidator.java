package com.fschoen.parlorplace.backend.validation.implementation;

import com.fschoen.parlorplace.backend.controller.dto.authentication.TokenRefreshRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSigninRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserSignupRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserUpdateRequestDTO;
import com.fschoen.parlorplace.backend.validation.BaseValidator;
import com.fschoen.parlorplace.backend.validation.ValidationResult;

public class UserValidator extends BaseValidator {

    public ValidationResult validate(UserSignupRequestDTO userSignupRequestDTO) {
        return new ValidationResult(validateConstraints(userSignupRequestDTO, false));
    }

    public ValidationResult validate(UserSigninRequestDTO userSigninRequestDTO) {
        return new ValidationResult(validateConstraints(userSigninRequestDTO, false));
    }

    public ValidationResult validate(TokenRefreshRequestDTO tokenRefreshRequestDTO) {
        return new ValidationResult(validateConstraints(tokenRefreshRequestDTO, false));
    }

    public ValidationResult validate(UserUpdateRequestDTO userUpdateRequestDTO) {
        return new ValidationResult(validateConstraints(userUpdateRequestDTO, true));
    }

}
