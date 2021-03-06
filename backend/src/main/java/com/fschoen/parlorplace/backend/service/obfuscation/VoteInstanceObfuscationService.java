package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteCollectionDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.Map;

public abstract class VoteInstanceObfuscationService<
        V extends VoteDTO<P, ?, C, ?, ?>,
        G extends GameDTO<?, ?, ?, ?>,
        P extends PlayerDTO<?>,
        C extends VoteCollectionDTO<?>,
        POServ extends PlayerObfuscationService<P, G, ?>,
        COServ extends VoteCollectionObfuscationService<C, ?, G, P, ?>
        > extends SingleObfuscationService<V, G> {

    private final POServ playerObfuscationService;
    private final COServ voteCollectionObfuscationService;

    public VoteInstanceObfuscationService(UserRepository userRepository, POServ playerObfuscationService, COServ voteCollectionObfuscationService) {
        super(userRepository);
        this.playerObfuscationService = playerObfuscationService;
        this.voteCollectionObfuscationService = voteCollectionObfuscationService;
    }

    @Override
    public void obfuscateFor(V v, User user, G g) {
        playerObfuscationService.obfuscateFor(v.getVoters(), user, g);
        for (Map.Entry<Long, C> entry : v.getVoteCollectionMap().entrySet()) {
            this.voteCollectionObfuscationService.obfuscateFor(entry.getValue(), user, g);
        }
    }

}
