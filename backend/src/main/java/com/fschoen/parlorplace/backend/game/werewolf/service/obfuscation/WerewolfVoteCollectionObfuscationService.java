package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfVoteCollectionDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.VoteCollectionExtendedObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WerewolfVoteCollectionObfuscationService extends VoteCollectionExtendedObfuscationService<
        WerewolfVoteCollectionDTO,
        WerewolfPlayerDTO,
        WerewolfGameDTO,
        WerewolfPlayerDTO,
        WerewolfPlayerObfuscationService,
        WerewolfPlayerObfuscationService
        > {

    @Autowired
    public WerewolfVoteCollectionObfuscationService(UserRepository userRepository, WerewolfPlayerObfuscationService playerObfuscationService, WerewolfPlayerObfuscationService targetObfuscationService) {
        super(userRepository, playerObfuscationService, targetObfuscationService);
    }

}
