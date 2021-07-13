package com.fschoen.parlorplace.backend.utility;

public interface Obfuscateable<T> {

    /**
     * Obfuscates the given object and removes information that is considered to be confidential in the usual case.
     *
     * @return The obfuscated object
     */
    public T obfuscate();

}
