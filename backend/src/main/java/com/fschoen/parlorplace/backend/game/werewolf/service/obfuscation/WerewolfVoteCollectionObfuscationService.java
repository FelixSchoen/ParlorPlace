package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerVoteCollectionDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.VoteCollectionObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WerewolfVoteCollectionObfuscationService extends VoteCollectionObfuscationService<
        WerewolfPlayerVoteCollectionDTO,
        WerewolfPlayerDTO,
        WerewolfGameDTO,
        WerewolfPlayerDTO,
        WerewolfPlayerObfuscationService
        > {

    @Autowired
    public WerewolfVoteCollectionObfuscationService(UserRepository userRepository, WerewolfPlayerObfuscationService targetObfuscationService) {
        super(userRepository, targetObfuscationService);
    }

}
