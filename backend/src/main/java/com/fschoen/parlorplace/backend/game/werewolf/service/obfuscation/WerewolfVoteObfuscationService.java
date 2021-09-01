package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerWerewolfVoteDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.VoteObfuscationService;
import org.springframework.stereotype.Service;

@Service
public class WerewolfVoteObfuscationService extends VoteObfuscationService<
        WerewolfVoteDTO<?,?>,
        WerewolfGameDTO> {

    private final WerewolfPlayerWerewolfVoteObfuscationService werewolfPlayerWerewolfVoteObfuscationService;

    public WerewolfVoteObfuscationService(UserRepository userRepository, WerewolfPlayerWerewolfVoteObfuscationService werewolfPlayerWerewolfVoteObfuscationService) {
        super(userRepository);
        this.werewolfPlayerWerewolfVoteObfuscationService = werewolfPlayerWerewolfVoteObfuscationService;
    }

    @Override
    public void obfuscateFor(WerewolfVoteDTO<?, ?> werewolfVoteDTO, User user, WerewolfGameDTO werewolfGameDTO) {
        switch(werewolfVoteDTO.getVoteIdentifier()) {
            case PLAYER_VOTE -> {
                this.werewolfPlayerWerewolfVoteObfuscationService.obfuscateFor((WerewolfPlayerWerewolfVoteDTO) werewolfVoteDTO, user, werewolfGameDTO);
            }
            default -> throw new NotImplementedException("Unknown Vote Identifier");
        }
    }

}
