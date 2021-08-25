package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.GameRoleDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;

public abstract class GameRoleObfuscationService<
        R extends GameRoleDTO,
        G extends GameDTO<?, ?, ?, ?>,
        P extends PlayerDTO<R>> extends DoubleObfuscationService<R, G, P> {

    public GameRoleObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

}
