package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

public abstract class SingleObfuscationService<T, U> extends ObfuscationService<T> {

    public SingleObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    /**
     * Calls the obfuscation with two elements with the second one as being {@code null}.
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     */
    @Override
    public void obfuscateFor(T t, User user) {
        this.obfuscateFor(t, user, null);
    }

    /**
     * Obfuscates an object {@param t} specifically for a given {@param user} and a given {@param u}.
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     * @param u    Additional information about the object to obfuscate
     */
    public abstract void obfuscateFor(T t, User user, U u);

    /**
     * Obfuscates all the objects in a list for a specific user and additional information given by {@param u}.
     *
     * @param tCollection List of objects to obfuscate
     * @param user  The user to obfuscate the objects for
     * @param u     Additional information about the object to obfuscate
     */
    public void obfuscateFor(Collection<T> tCollection, User user, U u) {
        tCollection.forEach(element -> this.obfuscateFor(element, user, u));
        tCollection.removeAll(Collections.singleton(null));
    }

}
