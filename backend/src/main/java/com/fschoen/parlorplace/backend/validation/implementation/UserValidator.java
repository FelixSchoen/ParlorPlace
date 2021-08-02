package com.fschoen.parlorplace.backend.validation.implementation;

import com.fschoen.parlorplace.backend.controller.dto.authentication.TokenRefreshRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserLoginRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserRegisterRequestDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserUpdateRequestDTO;
import com.fschoen.parlorplace.backend.validation.BaseValidator;
import com.fschoen.parlorplace.backend.validation.ValidationResult;

public class UserValidator extends BaseValidator {

    public ValidationResult validate(UserRegisterRequestDTO userRegisterRequestDTO) {
        return new ValidationResult(validateConstraints(userRegisterRequestDTO, false));
    }

    public ValidationResult validate(UserLoginRequestDTO userLoginRequestDTO) {
        return new ValidationResult(validateConstraints(userLoginRequestDTO, false));
    }

    public ValidationResult validate(TokenRefreshRequestDTO tokenRefreshRequestDTO) {
        return new ValidationResult(validateConstraints(tokenRefreshRequestDTO, false));
    }

    public ValidationResult validate(UserUpdateRequestDTO userUpdateRequestDTO) {
        return new ValidationResult(validateConstraints(userUpdateRequestDTO, true));
    }

}
