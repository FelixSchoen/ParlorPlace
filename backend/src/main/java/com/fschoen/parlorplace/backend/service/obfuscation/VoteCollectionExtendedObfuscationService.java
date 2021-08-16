package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

public abstract class VoteCollectionExtendedObfuscationService<
        C extends VoteCollectionDTO<P, T>,
        T,
        G extends GameDTO<?, ?, ?, ?>,
        P extends PlayerDTO<?>,
        TOServ extends ExtendedObfuscationService<T, G>
        > extends ExtendedObfuscationService<C, G> {

    private final TOServ targetObfuscationService;

    public VoteCollectionExtendedObfuscationService(UserRepository userRepository, TOServ targetObfuscationService) {
        super(userRepository);
        this.targetObfuscationService = targetObfuscationService;
    }

    @Override
    public void obfuscateFor(C c, User user, G g) {
        this.targetObfuscationService.obfuscateFor(c.getSubjects(), user, g);
        this.targetObfuscationService.obfuscateFor(c.getSelection(), user, g);
    }

}
