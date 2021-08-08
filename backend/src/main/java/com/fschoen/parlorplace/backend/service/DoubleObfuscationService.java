package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DoubleObfuscationService<T, U> extends ObfuscationService<T> {

    public DoubleObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    /**
     * Calls the obfuscation with two elements with the second one as being {@code null}.
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     * @return The obfuscated object
     */
    public T obfuscateFor(T t, User user) {
        return this.obfuscateFor(t, user, null);
    }

    /**
     * Obfuscates an object {@param t} specifically for a given {@param user} and a given {@param u}.
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     * @param u    Additional information about the object to obfuscate
     * @return The obfuscated object
     */
    public abstract T obfuscateFor(T t, User user, U u);

    /**
     * Obfuscates all the objects in a list for a specific user and additional information given by {@param u}.
     *
     * @param tList List of objects to obfuscate
     * @param user  The user to obfuscate the objects for
     *              * @param u    Additional information about the object to obfuscate
     * @return A list of obfuscated objects, specifically for the given user
     */
    public List<T> obfuscateFor(List<T> tList, User user, U u) {
        return tList.stream().map(element -> this.obfuscateFor(element, user, u)).collect(Collectors.toList());
    }

}
