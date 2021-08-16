package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

public abstract class PlayerExtendedObfuscationService<T extends PlayerDTO<?>, U> extends ExtendedObfuscationService<T, U> {

    private final ObfuscationService<UserDTO> userObfuscationService;

    public PlayerExtendedObfuscationService(UserRepository userRepository, ObfuscationService<UserDTO> userObfuscationService) {
        super(userRepository);
        this.userObfuscationService = userObfuscationService;
    }

    @Override
    public void obfuscateFor(T t, User user, U u) {
        this.obfuscateForInitial(t, user, u);
        this.userObfuscationService.obfuscate(t.getUser());
    }

    /**
     * Obfuscates an object {@param t} specifically for a given {@param user} and a given {@param u}, without obfuscating the user.
     *
     * @param t    The object to obfuscate
     * @param user The user to obfuscate the object for
     * @param u    Additional information about the object to obfuscate
     * @return The obfuscated object
     */
    public abstract void obfuscateForInitial(T t, User user, U u);

}
