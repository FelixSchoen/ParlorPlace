package com.fschoen.parlorplace.backend.exception;

public class GameEndException extends RuntimeException {

    public GameEndException(String message) {
        super(message);
    }

    public GameEndException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameEndException(Throwable cause) {
        super(cause);
    }

}
