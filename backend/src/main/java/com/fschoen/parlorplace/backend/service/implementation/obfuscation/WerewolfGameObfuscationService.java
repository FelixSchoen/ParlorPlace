package com.fschoen.parlorplace.backend.service.implementation.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.ObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class WerewolfGameObfuscationService extends ObfuscationService<WerewolfGameDTO> {

    private final ObfuscationService<WerewolfPlayerDTO> werewolfPlayerObfuscationService;

    @Autowired
    public WerewolfGameObfuscationService(
            UserRepository userRepository,
            ObfuscationService<WerewolfPlayerDTO> werewolfPlayerObfuscationService
    ) {
        super(userRepository);
        this.werewolfPlayerObfuscationService = werewolfPlayerObfuscationService;
    }

    @Override
    public WerewolfGameDTO obfuscateFor(WerewolfGameDTO werewolfGameDTO, User user) {
        Set<WerewolfPlayerDTO> obfuscatedPlayerDTOSet = new HashSet<>(this.werewolfPlayerObfuscationService.obfuscateFor(werewolfGameDTO.getPlayers().stream().toList(), user));
        return werewolfGameDTO.toBuilder().players(obfuscatedPlayerDTOSet).build();
    }

}
