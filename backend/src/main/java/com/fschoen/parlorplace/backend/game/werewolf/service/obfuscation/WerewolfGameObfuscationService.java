package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfLogEntryDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.ObfuscationService;
import com.fschoen.parlorplace.backend.service.obfuscation.SingleObfuscationService;
import com.fschoen.parlorplace.backend.service.obfuscation.VoteObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WerewolfGameObfuscationService extends ObfuscationService<WerewolfGameDTO> {

    private final SingleObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO> playerObfuscationService;
    private final SingleObfuscationService<WerewolfVoteDTO<?, ?>, WerewolfGameDTO> voteObfuscationService;
    private final SingleObfuscationService<WerewolfLogEntryDTO, WerewolfGameDTO> logEntryObfuscationService;

    @Autowired
    public WerewolfGameObfuscationService(
            UserRepository userRepository,
            SingleObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO> playerObfuscationService,
            VoteObfuscationService<WerewolfVoteDTO<?, ?>, WerewolfGameDTO> voteObfuscationService,
            SingleObfuscationService<WerewolfLogEntryDTO, WerewolfGameDTO> logEntryObfuscationService
    ) {
        super(userRepository);
        this.playerObfuscationService = playerObfuscationService;
        this.voteObfuscationService = voteObfuscationService;
        this.logEntryObfuscationService = logEntryObfuscationService;
    }

    @Override
    public void obfuscateFor(WerewolfGameDTO werewolfGameDTO, User user) {
        this.playerObfuscationService.obfuscateFor(werewolfGameDTO.getPlayers(), user, werewolfGameDTO);
        this.voteObfuscationService.obfuscateFor(werewolfGameDTO.getVotes(), user, werewolfGameDTO);
        this.logEntryObfuscationService.obfuscateFor(werewolfGameDTO.getLog(), user, werewolfGameDTO);
    }

}
