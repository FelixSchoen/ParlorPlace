package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.VoteDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.Collection;

public abstract class VoteObfuscationService<V extends VoteDTO<?, ?, ?, ?, ?>, G extends GameDTO<?, ?, ?, ?>> extends SingleObfuscationService<V, G> {

    public VoteObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public void obfuscateFor(Collection<V> vs, User user, G g) {
        super.obfuscateFor(vs, user, g);
        vs.removeIf(vote -> (
                vote.getVoters().stream().noneMatch(voter -> voter.getUser().getId().equals(user.getId()))
                        && g.getGameState() != GameState.CONCLUDED));
    }

}
