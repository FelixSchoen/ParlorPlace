package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerWerewolfVoteDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerVoteCollectionDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.VoteObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WerewolfPlayerWerewolfVoteObfuscationService extends VoteObfuscationService<
        WerewolfPlayerWerewolfVoteDTO,
        WerewolfGameDTO,
        WerewolfPlayerDTO,
        WerewolfPlayerVoteCollectionDTO,
        WerewolfPlayerObfuscationService,
        WerewolfVoteCollectionObfuscationService
        > {

    @Autowired
    public WerewolfPlayerWerewolfVoteObfuscationService(UserRepository userRepository, WerewolfPlayerObfuscationService playerObfuscationService, WerewolfVoteCollectionObfuscationService voteCollectionObfuscationService) {
        super(userRepository, playerObfuscationService, voteCollectionObfuscationService);
    }

}
