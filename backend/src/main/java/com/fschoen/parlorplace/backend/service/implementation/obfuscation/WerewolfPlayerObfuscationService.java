package com.fschoen.parlorplace.backend.service.implementation.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.ObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WerewolfPlayerObfuscationService extends ObfuscationService<WerewolfPlayerDTO> {

    @Autowired
    public WerewolfPlayerObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public WerewolfPlayerDTO obfuscateFor(WerewolfPlayerDTO werewolfPlayerDTO, User user) {
        if (werewolfPlayerDTO.getUser().getId().equals(user.getId()))
            return werewolfPlayerDTO;
        else
            return werewolfPlayerDTO.toBuilder().gameRole(null).build();
    }

}
