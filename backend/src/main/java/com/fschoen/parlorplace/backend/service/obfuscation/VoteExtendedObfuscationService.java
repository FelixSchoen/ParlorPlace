package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.Map;

public abstract class VoteExtendedObfuscationService<
        V extends VoteDTO<P, ?, C, ?>,
        G extends GameDTO<?, ?, ?, ?>,
        P extends PlayerDTO<?>,
        C extends VoteCollectionDTO<P, ?>,
        COServ extends VoteCollectionExtendedObfuscationService<C, ?, G, P, ?>
        > extends ExtendedObfuscationService<V, G> {

    private final COServ voteCollectionObfuscationService;

    public VoteExtendedObfuscationService(UserRepository userRepository, COServ voteCollectionObfuscationService) {
        super(userRepository);
        this.voteCollectionObfuscationService = voteCollectionObfuscationService;
    }

    @Override
    public void obfuscateFor(V v, User user, G g) {
        for (Map.Entry<Long,C> entry : v.getVoteCollectionMap().entrySet()) {
            this.voteCollectionObfuscationService.obfuscateFor(entry.getValue(), user, g);
        }
    }

}
