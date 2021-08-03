package com.fschoen.parlorplace.backend.utility.messaging;

import java.util.ResourceBundle;

public class Messages {

    public static String exception(String messageIdentifier) {
        return ResourceBundle.getBundle("strings.exception-explanations").getString(messageIdentifier);
    }

}
