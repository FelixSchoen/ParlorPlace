package com.fschoen.parlorplace.backend.service.obfuscation;

import com.fschoen.parlorplace.backend.controller.dto.game.GameDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.LogEntryDTO;
import com.fschoen.parlorplace.backend.controller.dto.game.PlayerDTO;
import com.fschoen.parlorplace.backend.entity.User;
import com.fschoen.parlorplace.backend.repository.UserRepository;

import java.util.Collection;

public abstract class LogEntryExtendedObfuscationService<
        L extends LogEntryDTO<P>,
        G extends GameDTO<?, ?, ?, ?>,
        P extends PlayerDTO<?>,
        POServ extends PlayerExtendedObfuscationService<P, G>
        > extends ExtendedObfuscationService<L, G> {

    private final POServ playerObfuscationService;

    public LogEntryExtendedObfuscationService(UserRepository userRepository, POServ playerObfuscationService) {
        super(userRepository);
        this.playerObfuscationService = playerObfuscationService;
    }

    @Override
    public void obfuscateFor(L logEntryDTO, User user, G gameDTO) {
        this.playerObfuscationService.obfuscateFor(logEntryDTO.getRecipients(), user, gameDTO);
        this.playerObfuscationService.obfuscateFor(logEntryDTO.getSources(), user, gameDTO);
        this.playerObfuscationService.obfuscateFor(logEntryDTO.getTargets(), user, gameDTO);
    }

    @Override
    public void obfuscateFor(Collection<L> ls, User user, G g) {
        super.obfuscateFor(ls, user, g);
        ls.removeIf(log -> log.getRecipients().stream().noneMatch(recipient -> recipient.getUser().getId().equals(user.getId())));
    }
}
