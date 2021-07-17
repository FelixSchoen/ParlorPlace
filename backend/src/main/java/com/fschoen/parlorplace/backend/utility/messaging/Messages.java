package com.fschoen.parlorplace.backend.utility.messaging;

import java.util.ResourceBundle;

public class Messages {

    public static String getExceptionExplanationMessage(String messageKey) {
        return ResourceBundle.getBundle("strings.exception-explanations").getString(messageKey);
    }

}
