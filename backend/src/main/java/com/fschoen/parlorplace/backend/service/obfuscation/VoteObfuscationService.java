package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.Collection;

public abstract class VoteObfuscationService<V extends VoteDTO<?, ?, ?, ?, ?>, G extends GameDTO<?, ?, ?, ?>> extends SingleObfuscationService<V, G> {

    public VoteObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public void obfuscateFor(Collection<V> vs, User user, G g) {
        super.obfuscateFor(vs, user, g);
        this.obfuscateForCallback(vs, user, g);
    }

    protected abstract void obfuscateForCallback(Collection<V> vs, User user, G g);

}
