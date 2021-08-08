package com.fschoen.parlorplace.backend.service;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ObfuscationService<T> extends BaseService {

    public ObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    /**
     * Removes information from {@param t} that is considered to be confidential for the current principal
     *
     * @param t The object to obfuscate
     * @return The obfuscated object
     */
    public T obfuscate(T t) {
        User principal = this.getPrincipal();
        return this.obfuscateFor(t, principal);
    }

    /**
     * Obfuscates all the objects in a list for the current principal
     *
     * @param tList List of objects to obfuscate
     * @return A list of obfuscated objects
     */
    public List<T> obfuscate(List<T> tList) {
        User principal = this.getPrincipal();
        return this.obfuscateFor(tList, principal);
    }

    /**
     * Obfuscates an object {@param t} specifically for a given {@param user}
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     * @return The obfuscated object
     */
    public abstract T obfuscateFor(T t, User user);

    /**
     * Obfuscates all the objects in a list for a specific user
     *
     * @param tList List of objects to obfuscate
     * @param user  The user to obfuscate the objects for
     * @return A list of obfuscated objects, specifically for the given user
     */
    public List<T> obfuscateFor(List<T> tList, User user) {
        return tList.stream().map(element -> this.obfuscateFor(element, user)).collect(Collectors.toList());
    }

}
