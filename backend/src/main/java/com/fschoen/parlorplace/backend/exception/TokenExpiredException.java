package com.fschoen.parlorplace.backend.exception;

import com.fschoen.parlorplace.backend.utility.messaging.*;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String token, String message) {
        super(String.format(Messages.exception("authorization.token.refresh.failed"), token, message));
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpiredException(Throwable cause) {
        super(cause);
    }

}
