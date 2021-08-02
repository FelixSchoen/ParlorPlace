package com.fschoen.parlorplace.backend.utility.messaging;

import com.fschoen.parlorplace.backend.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AuthorizationException.class})
    protected ResponseEntity<Object> handleAuthorizationException(AuthorizationException exception, WebRequest webRequest) {
        return errorMessage(exception, HttpStatus.UNAUTHORIZED, webRequest);
    }

    @ExceptionHandler(value = {DataConflictException.class})
    protected ResponseEntity<Object> handleDataConflictException(DataConflictException exception, WebRequest webRequest) {
        return errorMessage(exception, HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(value = {GameException.class})
    protected ResponseEntity<Object> handleGameException(GameException exception, WebRequest webRequest) {
        return errorMessage(exception, HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(value = {NotImplementedException.class})
    protected ResponseEntity<Object> handleNotImplementedException(AuthorizationException exception, WebRequest webRequest) {
        return errorMessage(exception, HttpStatus.NOT_IMPLEMENTED, webRequest);
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ValidationException exception, WebRequest webRequest) {
        return errorMessage(exception, HttpStatus.BAD_REQUEST, webRequest);
    }

    private ResponseEntity<Object> errorMessage(Exception exception, HttpStatus status, WebRequest webRequest) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), status, webRequest);
    }

}
