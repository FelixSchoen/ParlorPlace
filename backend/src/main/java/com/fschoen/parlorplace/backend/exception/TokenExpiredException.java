package com.fschoen.parlorplace.backend.exception;

import com.fschoen.parlorplace.backend.utility.messaging.MessageIdentifiers;
import com.fschoen.parlorplace.backend.utility.messaging.Messages;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String token, String message) {
        super(String.format(Messages.exception(MessageIdentifiers.AUTHORIZATION_TOKEN_REFRESH_FAILED), token, message));
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpiredException(Throwable cause) {
        super(cause);
    }

}
