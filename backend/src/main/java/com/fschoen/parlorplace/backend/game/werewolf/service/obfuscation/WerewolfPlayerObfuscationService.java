package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.user.UserDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.ObfuscationService;
import com.fschoen.parlorplace.backend.service.obfuscation.PlayerObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WerewolfPlayerObfuscationService extends PlayerObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO, WerewolfGameRoleObfuscationService> {

    @Autowired
    public WerewolfPlayerObfuscationService(UserRepository userRepository, ObfuscationService<UserDTO> userObfuscationService, WerewolfGameRoleObfuscationService gameRoleObfuscationService) {
        super(userRepository, userObfuscationService, gameRoleObfuscationService);
    }

    @Override
    public void obfuscateForInitial(WerewolfPlayerDTO werewolfPlayerDTO, User user, WerewolfGameDTO werewolfGameDTO) {
        if (werewolfGameDTO != null && werewolfGameDTO.getGameState() == GameState.CONCLUDED
                || werewolfPlayerDTO.getUser().getId().equals(user.getId())) {
            return;
        }

        werewolfPlayerDTO.setCodeName(null);

        if (werewolfPlayerDTO.getPlayerState() == PlayerState.DECEASED) {
            this.gameRoleObfuscationService.obfuscateFor(werewolfPlayerDTO.getGameRoles(), user, werewolfGameDTO, werewolfPlayerDTO);
            return;
        }

        werewolfPlayerDTO.setGameRoles(new ArrayList<>());
    }

}
