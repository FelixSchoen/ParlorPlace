package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

public abstract class VoteCollectionObfuscationService<
        C extends VoteCollectionDTO<P, T>,
        T,
        G extends GameDTO<?, ?, ?, ?>,
        P extends PlayerDTO<?>,
        TOServ extends SingleObfuscationService<T, G>
        > extends SingleObfuscationService<C, G> {

    private final TOServ targetObfuscationService;

    public VoteCollectionObfuscationService(UserRepository userRepository, TOServ targetObfuscationService) {
        super(userRepository);
        this.targetObfuscationService = targetObfuscationService;
    }

    @Override
    public void obfuscateFor(C c, User user, G g) {
        this.targetObfuscationService.obfuscateFor(c.getSubjects(), user, g);
        this.targetObfuscationService.obfuscateFor(c.getSelection(), user, g);
    }

}
