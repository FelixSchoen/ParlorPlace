package com.fschoen.parlorplace.backend.game.werewolf.service.obfuscation;

import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.enumeration.GameState;
import com.fschoen.parlorplace.backend.enumeration.PlayerState;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfGameDTO;
import com.fschoen.parlorplace.backend.game.werewolf.dto.game.WerewolfPlayerDTO;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import com.fschoen.parlorplace.backend.service.DoubleObfuscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WerewolfPlayerObfuscationService extends DoubleObfuscationService<WerewolfPlayerDTO, WerewolfGameDTO> {

    @Autowired
    public WerewolfPlayerObfuscationService(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public WerewolfPlayerDTO obfuscateFor(WerewolfPlayerDTO werewolfPlayerDTO, User user, WerewolfGameDTO werewolfGameDTO) {
        if (werewolfGameDTO != null && werewolfGameDTO.getGameState() == GameState.CONCLUDED) {
            return werewolfPlayerDTO;
        }
        else if (werewolfPlayerDTO.getUser().getId().equals(user.getId())
                || werewolfPlayerDTO.getPlayerState() == PlayerState.DECEASED)
            return werewolfPlayerDTO;
        else
            return werewolfPlayerDTO.toBuilder().codeName(null).gameRoles(null).build();
    }

}
