package com.fschoen.parlorplace.backend.utility.messaging;

import com.fschoen.parlorplace.backend.exception.AuthenticationException;
import com.fschoen.parlorplace.backend.exception.DataConflictException;
import com.fschoen.parlorplace.backend.exception.GameException;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.exception.ValidationException;
import com.fschoen.parlorplace.backend.exception.VoteException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<Object> handleAuthorizationException(AuthenticationException exception, WebRequest webRequest) {
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

    @ExceptionHandler(value = {VoteException.class})
    protected ResponseEntity<Object> handleGameException(VoteException exception, WebRequest webRequest) {
        return errorMessage(exception, HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(value = {NotImplementedException.class})
    protected ResponseEntity<Object> handleNotImplementedException(AuthenticationException exception, WebRequest webRequest) {
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
