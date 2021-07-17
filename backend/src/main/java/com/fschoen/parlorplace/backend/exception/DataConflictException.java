package com.fschoen.parlorplace.backend.exception;

/**
 * Represents an exception caused by data that conflicts with the database (e.g. username already exists).
 */
public class DataConflictException extends RuntimeException {

    public DataConflictException(String message) {
        super(message);
    }

    public DataConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataConflictException(Throwable cause) {
        super(cause);
    }

}
