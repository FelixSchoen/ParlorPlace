package com.fschoen.parlorplace.backend.exceptions;

import com.fschoen.parlorplace.backend.utility.Messages;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String token, String message) {
        super(String.format(Messages.getExceptionExplanationMessage("authorization.token.refresh.failed"), token, message));
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpiredException(Throwable cause) {
        super(cause);
    }

}
