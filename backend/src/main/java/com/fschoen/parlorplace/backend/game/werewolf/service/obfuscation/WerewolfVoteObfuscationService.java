package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteCollectionDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.VoteObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WerewolfVoteObfuscationService extends VoteObfuscationService<
        WerewolfVoteDTO,
        WerewolfGameDTO,
        WerewolfPlayerDTO,
        WerewolfVoteCollectionDTO,
        WerewolfPlayerObfuscationService,
        WerewolfVoteCollectionObfuscationService
        > {

    @Autowired
    public WerewolfVoteObfuscationService(UserRepository userRepository, WerewolfPlayerObfuscationService playerObfuscationService, WerewolfVoteCollectionObfuscationService voteCollectionObfuscationService) {
        super(userRepository, playerObfuscationService, voteCollectionObfuscationService);
    }

}
