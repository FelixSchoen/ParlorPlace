package com.fschoen.parlorplace.backend.validation;

import com.fschoen.parlorplace.backend.exception.ValidationException;

import java.util.List;

public class ValidationResult {

    private List<String> violations;

    public ValidationResult(List<String> violations) {
        this.violations = violations;
    }

    public void throwIfInvalid() throws ValidationException {
        if (!this.isValid()) {
            throw new ValidationException(this.toExceptionMessage());
        }
    }

    private String toExceptionMessage() {
        return String.join(", ", this.violations);
    }

    private boolean isValid() {
        return this.violations.size() == 0;
    }

}
