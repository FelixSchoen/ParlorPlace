package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

public abstract class DoubleObfuscationService<T, U, V> extends SingleObfuscationService<T, U> {

    public DoubleObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    /**
     * Calls the obfuscation with three elements with the second and third one as being {@code null}.
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     */
    @Override
    public void obfuscateFor(T t, User user) {
        this.obfuscateFor(t, user, null, null);
    }

    /**
     * Calls the obfuscation with three elements with the second and third one as being {@code null}.
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     */
    @Override
    public void obfuscateFor(T t, User user, U u) {
        this.obfuscateFor(t, user, null, null);
    }

    /**
     * Obfuscates an object {@param t} specifically for a given {@param user}, a given {@param u} and a given {@param v}.
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     * @param u    Additional information about the object to obfuscate
     * @param v    Additional information about the object to obfuscate
     */
    public abstract void obfuscateFor(T t, User user, U u, V v);

    /**
     * Obfuscates all the objects in a list for a specific user and additional information given by {@param u} and {@param v}.
     *
     * @param tCollection List of objects to obfuscate
     * @param user  The user to obfuscate the objects for
     * @param u     Additional information about the object to obfuscate
     * @param v     Additional information about the object to obfuscate
     */
    public void obfuscateFor(Collection<T> tCollection, User user, U u, V v) {
        tCollection.forEach(element -> this.obfuscateFor(element, user, u, v));
        tCollection.removeAll(Collections.singleton(null));
    }

}
