package com.fschoen.parlorplace.backend.validation;

import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifier;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class BaseValidator {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    /**
     * Validates the given entity against its defined constraints.
     *
     * @param entity         The entity to validate
     * @param disregardEmpty Boolean that indicates if constraints on a field should be ignored if the field is {@code null} or empty
     *                       and no {@code @NotNull} and {@code @NotBlank} annotation is on the field
     * @param exceptions     An array representing exceptions to the properties to validate
     * @param <T>            The type of the entity
     * @return A {@link List<String>} where each message contains an explanation to the violation of the constraints
     */
    protected <T> List<String> validateConstraints(T entity, boolean disregardEmpty, String... exceptions) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        List<String> violatedConstraints = new ArrayList<>();

        for (ConstraintViolation<T> violation : violations) {
            if (disregardEmpty && violation.getInvalidValue() == null && !(violation.getConstraintDescriptor().getAnnotation() instanceof NotNull))
                continue;
            if (disregardEmpty && violation.getInvalidValue() == "" && !(violation.getConstraintDescriptor().getAnnotation() instanceof NotBlank))
                continue;
            if (Arrays.stream(exceptions).noneMatch(x -> violation.getPropertyPath().toString().equals(x))) {
                violatedConstraints.add(getMessage(violation.getPropertyPath(), violation.getMessage()));
            }
        }

        return violatedConstraints;
    }

    private String getMessage(Object property, Object message) {
        return String.format(Messages.exception(MessageIdentifier.VALIDATION_MESSAGE_TEMPLATE), property, message);
    }

}
