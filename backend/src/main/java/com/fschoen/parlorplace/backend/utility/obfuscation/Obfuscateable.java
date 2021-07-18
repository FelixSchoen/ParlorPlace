package com.fschoen.parlorplace.backend.utility.obfuscation;

public interface Obfuscateable {

    /**
     * Obfuscates the given object and removes information that is considered to be confidential in the usual case.
     */
    void obfuscate();

}
