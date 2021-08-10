package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.ObfuscationService;
import com.fschoen.parlorplace.backend.service.obfuscation.PlayerDoubleObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WerewolfPlayerObfuscationService extends PlayerDoubleObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO> {

    @Autowired
    public WerewolfPlayerObfuscationService(UserRepository userRepository, ObfuscationService<UserDTO> userObfuscationService) {
        super(userRepository, userObfuscationService);
    }

    @Override
    public WerewolfPlayerDTO obfuscateForInitial(WerewolfPlayerDTO werewolfPlayerDTO, User user, WerewolfGameDTO werewolfGameDTO) {
        if (werewolfGameDTO != null && werewolfGameDTO.getGameState() == GameState.CONCLUDED
                || werewolfPlayerDTO.getUser().getId().equals(user.getId())
                || werewolfPlayerDTO.getPlayerState() == PlayerState.DECEASED) {
            return werewolfPlayerDTO;
        }

        return werewolfPlayerDTO.toBuilder().codeName(null).gameRoles(new ArrayList<>()).build();
    }

}
