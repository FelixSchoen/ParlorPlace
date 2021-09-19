package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.exception.NotImplementedException;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerWerewolfVoteDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.VoteObfuscationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class WerewolfVoteObfuscationService extends VoteObfuscationService<
        WerewolfVoteDTO<?, ?>,
        WerewolfGameDTO> {

    private final WerewolfPlayerWerewolfVoteObfuscationService werewolfPlayerWerewolfVoteObfuscationService;

    public WerewolfVoteObfuscationService(UserRepository userRepository, WerewolfPlayerWerewolfVoteObfuscationService werewolfPlayerWerewolfVoteObfuscationService) {
        super(userRepository);
        this.werewolfPlayerWerewolfVoteObfuscationService = werewolfPlayerWerewolfVoteObfuscationService;
    }

    @Override
    public void obfuscateFor(WerewolfVoteDTO<?, ?> werewolfVoteDTO, User user, WerewolfGameDTO werewolfGameDTO) {
        switch (werewolfVoteDTO.getVoteIdentifier()) {
            case PLAYER_VOTE -> {
                this.werewolfPlayerWerewolfVoteObfuscationService.obfuscateFor((WerewolfPlayerWerewolfVoteDTO) werewolfVoteDTO, user, werewolfGameDTO);
            }
            default -> throw new NotImplementedException("Unknown Vote Identifier");
        }
    }

    @Override
    protected void obfuscateForCallback(Collection<WerewolfVoteDTO<?, ?>> werewolfVoteDTOS, User user, WerewolfGameDTO werewolfGameDTO) {
        Optional<WerewolfPlayerDTO> optionalPlayer = werewolfGameDTO.getPlayers().stream().filter(player -> player.getUser().getId().equals(user.getId())).findAny();
        werewolfVoteDTOS.removeIf(vote -> (
                vote.getVoters().stream().noneMatch(voter -> voter.getUser().getId().equals(user.getId())) && werewolfGameDTO.getGameState() != GameState.CONCLUDED)
                && (optionalPlayer.isEmpty() || optionalPlayer.get().getPlayerState() == PlayerState.ALIVE));
    }

}
