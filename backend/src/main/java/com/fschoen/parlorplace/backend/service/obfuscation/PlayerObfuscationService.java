package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

public abstract class PlayerObfuscationService<
        P extends PlayerDTO<?>,
        U,
        ROServ extends GameRoleObfuscationService<?,?, P>> extends SingleObfuscationService<P, U> {

    private final ObfuscationService<UserDTO> userObfuscationService;
    protected final ROServ gameRoleObfuscationService;

    public PlayerObfuscationService(UserRepository userRepository, ObfuscationService<UserDTO> userObfuscationService, ROServ gameRoleObfuscationService) {
        super(userRepository);
        this.userObfuscationService = userObfuscationService;
        this.gameRoleObfuscationService = gameRoleObfuscationService;
    }

    @Override
    public void obfuscateFor(P p, User user, U u) {
        this.obfuscateForInitial(p, user, u);
        this.userObfuscationService.obfuscate(p.getUser());
    }

    /**
     * Obfuscates an object {@param p} specifically for a given {@param user} and a given {@param u}, without obfuscating the user.
     * This serves the purpose that for future implementations, obfuscating the user cannot be forgotten.
     *
     * @param p    The object to obfuscate
     * @param user The user to obfuscate the object for
     * @param u    Additional information about the object to obfuscate
     */
    public abstract void obfuscateForInitial(P p, User user, U u);

}
