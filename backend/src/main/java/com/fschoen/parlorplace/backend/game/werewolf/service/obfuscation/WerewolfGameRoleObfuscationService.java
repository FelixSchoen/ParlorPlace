package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.BodyguardWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.CupidWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.role.WitchWerewolfGameRoleDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.obfuscation.GameRoleObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class WerewolfGameRoleObfuscationService extends GameRoleObfuscationService<
        WerewolfGameRoleDTO,
        WerewolfGameDTO,
        WerewolfPlayerDTO> {

    @Autowired
    public WerewolfGameRoleObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public void obfuscateFor(WerewolfGameRoleDTO werewolfGameRoleDTO, User user, WerewolfGameDTO werewolfGameDTO, WerewolfPlayerDTO werewolfPlayerDTO) {
        if (werewolfGameDTO != null && werewolfGameDTO.getGameState() == GameState.CONCLUDED
                || werewolfPlayerDTO != null && werewolfPlayerDTO.getUser().getId().equals(user.getId())) {
            return;
        }

        switch (werewolfGameRoleDTO.getWerewolfRoleType()) {
            case WITCH -> {
                WitchWerewolfGameRoleDTO witchWerewolfGameRoleDTO = (WitchWerewolfGameRoleDTO) werewolfGameRoleDTO;
                witchWerewolfGameRoleDTO.setHasHealed(null);
                witchWerewolfGameRoleDTO.setHasKilled(null);
            }
            case CUPID -> {
                CupidWerewolfGameRoleDTO cupidWerewolfGameRoleDTO = (CupidWerewolfGameRoleDTO) werewolfGameRoleDTO;
                cupidWerewolfGameRoleDTO.setHasLinked(null);
            }
            case BODYGUARD -> {
                BodyguardWerewolfGameRoleDTO bodyguardWerewolfGameRoleDTO = (BodyguardWerewolfGameRoleDTO) werewolfGameRoleDTO;
                bodyguardWerewolfGameRoleDTO.setLastProtected(null);
            }
        }

        if (werewolfPlayerDTO != null && werewolfPlayerDTO.getPlayerState() == PlayerState.DECEASED) {
            return;
        }

        // Remove all information
        werewolfGameRoleDTO.setWerewolfRoleType(null);
        werewolfGameRoleDTO.setWerewolfFaction(null);
    }

    @Override
    public void obfuscateFor(Collection<WerewolfGameRoleDTO> werewolfGameRoleDTOS, User user, WerewolfGameDTO werewolfGameDTO, WerewolfPlayerDTO werewolfPlayerDTO) {
        super.obfuscateFor(werewolfGameRoleDTOS, user, werewolfGameDTO, werewolfPlayerDTO);
        werewolfGameRoleDTOS.removeIf(role -> (
                role.getWerewolfRoleType() == null && werewolfGameDTO.getGameState() != GameState.CONCLUDED
        ));
    }

}
