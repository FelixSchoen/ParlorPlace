package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.BaseService;

import java.util.Collection;
import java.util.Collections;

public abstract class ObfuscationService<T> extends BaseService {

    public ObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    /**
     * Removes information from {@param t} that is considered to be confidential for the current principal
     *
     * @param t The object to obfuscate
     */
    public void obfuscate(T t) {
        User principal = this.getPrincipal();
        this.obfuscateFor(t, principal);
    }

    /**
     * Obfuscates all the objects in a list for the current principal
     *
     * @param tCollection List of objects to obfuscate
     */
    public void obfuscate(Collection<T> tCollection) {
        User principal = this.getPrincipal();
        this.obfuscateFor(tCollection, principal);
    }

    /**
     * Obfuscates an object {@param t} specifically for a given {@param user}
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     */
    public abstract void obfuscateFor(T t, User user);

    /**
     * Obfuscates all the objects in a collection for a specific user
     *
     * @param tCollection List of objects to obfuscate
     * @param user  The user to obfuscate the objects for
     */
    public void obfuscateFor(Collection<T> tCollection, User user) {
        tCollection.forEach(element -> this.obfuscateFor(element, user));
        tCollection.removeAll(Collections.singleton(null));
    }

}
