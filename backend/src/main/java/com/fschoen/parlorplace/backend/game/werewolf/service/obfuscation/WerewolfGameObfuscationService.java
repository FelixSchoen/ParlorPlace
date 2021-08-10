package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfLogEntryDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.DoubleObfuscationService;
import com.fschoen.parlorplace.backend.service.obfuscation.ObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WerewolfGameObfuscationService extends ObfuscationService<WerewolfGameDTO> {

    private final DoubleObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO> werewolfPlayerObfuscationService;
    private final DoubleObfuscationService<WerewolfLogEntryDTO, WerewolfGameDTO> werewolfLogEntryObfuscationService;

    @Autowired
    public WerewolfGameObfuscationService(
            UserRepository userRepository,
            DoubleObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO> werewolfPlayerObfuscationService,
            DoubleObfuscationService<WerewolfLogEntryDTO, WerewolfGameDTO> werewolfLogEntryObfuscationService
    ) {
        super(userRepository);
        this.werewolfPlayerObfuscationService = werewolfPlayerObfuscationService;
        this.werewolfLogEntryObfuscationService = werewolfLogEntryObfuscationService;
    }

    @Override
    public WerewolfGameDTO obfuscateFor(WerewolfGameDTO werewolfGameDTO, User user) {
        Set<WerewolfPlayerDTO> obfuscatedPlayerDTOSet = new HashSet<>(this.werewolfPlayerObfuscationService.obfuscateFor(werewolfGameDTO.getPlayers().stream().toList(), user, werewolfGameDTO));
        List<WerewolfLogEntryDTO> obfuscatedLogEntryDTOList = this.werewolfLogEntryObfuscationService.obfuscateFor(werewolfGameDTO.getLog(), user, werewolfGameDTO);
        return werewolfGameDTO.toBuilder().players(obfuscatedPlayerDTOSet).log(obfuscatedLogEntryDTOList).build();
    }

}
