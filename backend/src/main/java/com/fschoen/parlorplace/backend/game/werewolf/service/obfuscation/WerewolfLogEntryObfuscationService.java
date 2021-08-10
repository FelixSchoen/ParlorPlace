package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfLogEntryDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.LogEntryExtendedObfuscationService;
import org.springframework.stereotype.Service;

@Service
public class WerewolfLogEntryObfuscationService extends LogEntryExtendedObfuscationService<WerewolfLogEntryDTO, WerewolfGameDTO, WerewolfPlayerDTO, WerewolfPlayerObfuscationService> {

    public WerewolfLogEntryObfuscationService(UserRepository userRepository, WerewolfPlayerObfuscationService playerObfuscationService) {
        super(userRepository, playerObfuscationService);
    }

}
