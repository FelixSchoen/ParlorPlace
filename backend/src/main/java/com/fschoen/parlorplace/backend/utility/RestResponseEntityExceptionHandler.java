package com.fschoen.parlorplace.backend.utility;

import com.fschoen.parlorplace.backend.exceptions.DataConflictException;
import com.fschoen.parlorplace.backend.exceptions.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DataConflictException.class})
    protected ResponseEntity<Object> handleInvalidDataException(DataConflictException exception, WebRequest webRequest) {
        return errorMessage(exception, HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ValidationException exception, WebRequest webRequest) {
        return errorMessage(exception, HttpStatus.BAD_REQUEST, webRequest);
    }

    private ResponseEntity<Object> errorMessage(Exception exception, HttpStatus status, WebRequest webRequest) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), status, webRequest);
    }

}
